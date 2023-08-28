--Ограничение на возраст
ALTER TABLE student
    ADD CONSTRAINT age_constraint CHECK ( age > 16 );
--Ограничение на уникальное имя студента
ALTER TABLE student ADD CONSTRAINT name_constraint UNIQUE (name);
    ALTER TABLE student ALTER COLUMN name SET  NOT NULL;
--Уникальная пара названия факультета и цвет факультета
ALTER TABLE faculty
    ADD CONSTRAINT name_color_constraint UNIQUE (name, color);
--Значение по умолчанию для возраста студента
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;

--#################

-- Создание таблицы Person
CREATE TABLE Person (
    person_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    has_license BOOLEAN NOT NULL
);
-- Создание таблицы Car
CREATE TABLE Car (
    car_id SERIAL PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    cost INT NOT NULL
);
-- Создание связующей таблицы PersonCar
CREATE TABLE Person_Car (
    person_id INT REFERENCES Person(person_id),
    car_id INT REFERENCES Car(car_id),
    PRIMARY KEY (person_id, car_id)
);

--#################

-- Получить информацию о студентах и их факультетах
select s.name, s.age, f.name from student s
left join faculty f on s.faculty_id = f.id;

-- Получить студентов, у которых есть аватарки
select s.name, s.age, a.id from student s
join avatar a on s.id = a.student