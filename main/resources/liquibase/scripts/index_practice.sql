--liquibase formatted sql
--changeset beshik7:index_practice
CREATE INDEX student_name_index ON student(name);
CREATE INDEX faculty_name_color_index ON faculty(name, color);
