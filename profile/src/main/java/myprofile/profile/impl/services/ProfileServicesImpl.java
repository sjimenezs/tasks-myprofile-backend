package myprofile.profile.impl.services;

import myprofile.common.dto.CheckUserRequest;
import myprofile.common.dto.CheckUserResponse;
import myprofile.common.error.ErrorCodes;
import myprofile.common.model.*;
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
    private final TorreUserDAO torreUserDAO;
    private final TorreJobDAO torreJobDAO;
    private final JobSyncDAO jobSyncDAO;
    private final JobDAO jobDAO;
    private final SkillDAO skillDAO;
    private final JobSkillDAO jobSkillDAO;
    private final UserDAO userDAO;
    private final UserSkillDAO userSkillDAO;
    private final ProfileConnectionFactory connectionFactory;


    public ProfileServicesImpl(TorreUserDAO torreUserDAO, TorreJobDAO torreJobDAO, JobSyncDAO jobSyncDAO, JobDAO jobDAO, SkillDAO skillDAO, JobSkillDAO jobSkillDAO, UserDAO userDAO, UserSkillDAO userSkillDAO, ProfileConnectionFactory connectionFactory) {
        this.torreUserDAO = torreUserDAO;
        this.torreJobDAO = torreJobDAO;
        this.jobSyncDAO = jobSyncDAO;
        this.jobDAO = jobDAO;
        this.skillDAO = skillDAO;
        this.jobSkillDAO = jobSkillDAO;
        this.userDAO = userDAO;
        this.userSkillDAO = userSkillDAO;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Result<CheckUserResponse> login(CheckUserRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isEmpty()) {
            return error(ErrorCodes.USERNAME_REQUIRED);
        }
        var resultFetchUser = this.torreUserDAO.fetchUser(request.getUsername());
        if (resultFetchUser.isError()) {
            return error(resultFetchUser);
        }
        var res = new CheckUserResponse();
        var user = resultFetchUser.ok();
        if (user.isPresent()) {
            res.setExists(true);
            var resultRegisterUser = this.registerUser(user.get());
            if (resultRegisterUser.isError()) {
                return error(resultRegisterUser);
            }
        } else {
            res.setExists(false);
        }
        return ok(res);
    }

    private Result<Boolean> registerUser(User user) {
        var resultOpenTransaction = this.connectionFactory.openTransaction();
        if (resultOpenTransaction.isError()) {
            return error(resultOpenTransaction);
        }

        try (var session = resultOpenTransaction.ok()) {
            var resultCurrentSkills = this.skillDAO.fetchAll(session);
            if (resultCurrentSkills.isError()) {
                return error(resultCurrentSkills);
            }

            var currentSkills = resultCurrentSkills.ok().stream().collect(Collectors.toMap(k -> k.getName().toLowerCase(), v -> v));

            var resultFetchUser = this.userDAO.fetchByUsername(session, user.getUsername());
            if (resultFetchUser.isError()) {
                return error(resultFetchUser);
            }

            if (resultFetchUser.ok().isEmpty()) {
                var resultSaveUser = this.userDAO.save(session, user);
                if (resultSaveUser.isError()) {
                    return error(resultFetchUser);
                }
            }

            var resultSaveUserSkills = this.saveUserSkills(session, user, currentSkills);
            if (resultSaveUserSkills.isError()) {
                return error(resultSaveUserSkills);
            }

            session.commit();

        }

        return ok(true);
    }

    private Result<Boolean> saveUserSkills(SqlSession session, User user, Map<String, Skill> alreadyProcessedSkills) {
        if (user.getSkills() == null) {
            return ok(true);
        }
        var resultFetchAlreadyUserSkills = this.userSkillDAO.fetchAllIdsByIdUser(session, user.getId());
        if (resultFetchAlreadyUserSkills.isError()) {
            return error(resultFetchAlreadyUserSkills);
        }
        var alreayUserSkills = new HashSet<Integer>(resultFetchAlreadyUserSkills.ok());
        for (var userSkill : user.getSkills()) {
            var skill = alreadyProcessedSkills.get(userSkill.getSkillName().toLowerCase());
            if (skill == null) {
                skill = new Skill();
                skill.setName(userSkill.getSkillName());
                var rSaveSkill = this.skillDAO.save(session, skill);
                if (rSaveSkill.isError()) {
                    return error(rSaveSkill);
                }
                skill.setId(rSaveSkill.ok());
                alreadyProcessedSkills.put(userSkill.getSkillName().toLowerCase(), skill);
            }
            if (alreayUserSkills.contains(skill.getId())) {
                continue;
            }

            alreayUserSkills.add(skill.getId());

            userSkill.setIdUser(user.getId());
            userSkill.setIdSkill(skill.getId());
            this.userSkillDAO.save(session, userSkill);
        }
        return ok(true);
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

        var jobSync = resultFetchJobSyncRecord.ok();

        return ok(!jobSync.isPresent() || totalJobsAtTorre > jobSync.get().getTotal());
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void syncJobsOfferTask() {
        var rHaveToSync = this.haveToSyncJobs();
        if (rHaveToSync.isError()) {
            return;
        }
        if (rHaveToSync.ok() != null && rHaveToSync.ok()) {
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

            session.commit();
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
        var jobsIterator = new JobInterator(offset -> this.torreJobDAO.fetchJobs(500, offset), initialOffset);
        return ok(jobsIterator);
    }
}
