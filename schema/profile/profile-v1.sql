--liquibase formatted sql

--changeset samueljimenez:job-table
create table job(
id varchar(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
name varchar(500) not null,
jobdate DATETIME not null,
primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--changeset samueljimenez:skill-table
create table skill(
id int auto_increment not null,
name varchar(500) not null,
primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--changeset samueljimenez:jobskill-table
create table jobskill(
id bigint auto_increment not null,
idjob varchar(36) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
idskill int not null,
experience varchar(500),
primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--changeset samueljimenez:jobsync-table
create table jobsync(
date DATETIME not null,
lastposition int not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--changeset samueljimenez:jobsync-total-table
alter table jobsync add total int not null;

--changeset samueljimenez:jobsync-name-change
alter table job drop column name;
alter table job add objective varchar(5000) null;

--changeset samueljimenez:jobsync-jobdate-remove
alter table job drop column jobdate;