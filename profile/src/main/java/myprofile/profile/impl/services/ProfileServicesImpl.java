package myprofile.profile.impl.services;

import myprofile.common.dto.CheckUserRequest;
import myprofile.common.dto.CheckUserResponse;
import myprofile.common.error.ErrorCodes;
import myprofile.common.model.Job;
import myprofile.common.model.JobSync;
import myprofile.common.model.Skill;
import myprofile.common.result.Result;

import myprofile.profile.dao.*;
import myprofile.services.ProfileServices;
import org.apache.ibatis.session.SqlSession;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static myprofile.common.result.Result.error;
import static myprofile.common.result.Result.ok;

public class ProfileServicesImpl implements ProfileServices {
    private final UserDAO userDAO;
    private final TorreJobDAO torreJobDAO;
    private final JobSyncDAO jobSyncDAO;
    private final JobDAO jobDAO;
    private final SkillDAO skillDAO;
    private final JobSkillDAO jobSkillDAO;
    private final ProfileConnectionFactory connectionFactory;


    public ProfileServicesImpl(UserDAO userDAO, TorreJobDAO torreJobDAO, JobSyncDAO jobSyncDAO, JobDAO jobDAO, SkillDAO skillDAO, JobSkillDAO jobSkillDAO, ProfileConnectionFactory connectionFactory) {
        this.userDAO = userDAO;
        this.torreJobDAO = torreJobDAO;
        this.jobSyncDAO = jobSyncDAO;
        this.jobDAO = jobDAO;
        this.skillDAO = skillDAO;
        this.jobSkillDAO = jobSkillDAO;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Result<CheckUserResponse> checkUser(CheckUserRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isEmpty()) {
            return error(ErrorCodes.USERNAME_REQUIRED);
        }
        var resultFetchUser = this.userDAO.fetchUser(request.getUsername());
        if (resultFetchUser.isError()) {
            return error(resultFetchUser);
        }
        var res = new CheckUserResponse();
        res.setExists(resultFetchUser.ok().isPresent());
        return ok(res);
    }

    private Result<Optional<JobSync>> fetchJobSyncRecord() {
        var resultOpenSession = this.connectionFactory.openSession();
        if (resultOpenSession.isError()) {
            return error(resultOpenSession);
        }
        try (var session = resultOpenSession.ok()) {
            return this.jobSyncDAO.fetch(session);
        } catch (Exception e) {
            e.printStackTrace();
            return error(ErrorCodes.JOBSYNC_FETCH_ERROR);
        }
    }

    public Result<Boolean> haveToSyncJobs() {
        var resultTotalJobsAtTorre = this.torreJobDAO.fetchJobsCount();
        if (resultTotalJobsAtTorre.isError()) {
            return error(resultTotalJobsAtTorre);
        }
        var totalJobsAtTorre = resultTotalJobsAtTorre.ok();

        var resultFetchJobSyncRecord = this.fetchJobSyncRecord();
        if (resultFetchJobSyncRecord.isError()) {
            return error(resultFetchJobSyncRecord);
        }

        if (resultFetchJobSyncRecord.ok().isEmpty()) {
            return ok(true);
        }

        if (totalJobsAtTorre > resultFetchJobSyncRecord.ok().get().getTotal()) {
            return ok(true);
        }

        return ok(false);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void syncJobsOfferTask() {
        var rHaveToSync = this.haveToSyncJobs();
        if (rHaveToSync.isError()) {
            return;
        }
        if (rHaveToSync.ok()) {
            this.syncJobsFromTorre(0);
        }
    }

    private synchronized void syncJobsFromTorre(int initalOffset) {
        var resultJobsFromTorre = this.fetchJobsFromTorre(initalOffset);
        if (resultJobsFromTorre.isError()) {
            return;
        }
        var jobsIterator = resultJobsFromTorre.ok();
        var resultOpenTransaction = this.connectionFactory.openTransaction();
        if (resultOpenTransaction.isError()) {
            return;
        }

        try (var session = resultOpenTransaction.ok()) {
            var resultCurrentSkills = this.skillDAO.fetchAll(session);
            if (resultCurrentSkills.isError()) {
                return;
            }

            var currentSkills = resultCurrentSkills.ok().stream().collect(Collectors.toMap(k -> k.getName().toLowerCase(), v -> v));

            var resultCurrentIdJobs = this.jobDAO.fetchAllIds(session);
            if (resultCurrentIdJobs.isError()) {
                return;
            }
            var alreadyInsertedJobIds = new HashSet<>(resultCurrentIdJobs.ok());

            var recordCounter = 0;
            while (jobsIterator.hasNext()) {
                var jobFromTorre = jobsIterator.next();
                System.out.println(jobFromTorre.getId());
                if (alreadyInsertedJobIds.contains(jobFromTorre.getId())) {
                    continue;
                }

                alreadyInsertedJobIds.add(jobFromTorre.getId());

                var rInsert = this.jobDAO.save(session, jobFromTorre);
                if (rInsert.isError()) {
                    return;
                }

                var resultSaveJobSkills = this.saveJobSkills(session, jobFromTorre, currentSkills);
                if (resultSaveJobSkills.isError()) {
                    return;
                }

                recordCounter++;

                if (recordCounter % 100 == 0) {
                    session.commit();
                }
            }

        }
    }

    private Result<Boolean> saveJobSkills(SqlSession session, Job job, Map<String, Skill> alreadyProcessedSkills) {
        if (job.getSkills() == null) {
            return ok(true);
        }
        for (var jobSkill : job.getSkills()) {
            var skill = alreadyProcessedSkills.get(jobSkill.getSkillName().toLowerCase());
            if (skill == null) {
                skill = new Skill();
                skill.setName(jobSkill.getSkillName());
                var rSaveSkill = this.skillDAO.save(session, skill);
                if (rSaveSkill.isError()) {
                    return error(rSaveSkill);
                }
                skill.setId(rSaveSkill.ok());
                alreadyProcessedSkills.put(jobSkill.getSkillName().toLowerCase(), skill);
            }

            jobSkill.setIdJob(job.getId());
            jobSkill.setIdSkill(skill.getId());
            this.jobSkillDAO.save(session, jobSkill);
        }
        return ok(true);
    }

    private Result<Iterator<Job>> fetchJobsFromTorre(int initialOffset) {
        var jobsIterator = new JobInterator((offset) -> this.torreJobDAO.fetchJobs(500, offset), initialOffset);
        return ok(jobsIterator);
    }
}
