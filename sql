WITH job_intervals AS (
    SELECT
        startjob,
        endjob,
        DATE(startjob) AS start_date,
        DATE(endjob) AS end_date,
        TIME(startjob) AS start_time,
        TIME(endjob) AS end_time
    FROM
        your_jobs_table
),
working_days AS (
    SELECT
        date
    FROM (
        SELECT DISTINCT DATE(startjob) AS date FROM job_intervals
        UNION
        SELECT DISTINCT DATE(endjob) AS date FROM job_intervals
    ) AS all_dates
    WHERE
        WEEKDAY(date) < 5 -- Hafta sonları hariç
        AND date NOT IN (SELECT holiday_date FROM holidays) -- Tatil günleri hariç
),
work_intervals AS (
    SELECT
        j.startjob,
        j.endjob,
        j.start_date,
        j.end_date,
        CASE
            WHEN j.start_date = j.end_date THEN
                TIMESTAMPDIFF(
                    MINUTE,
                    GREATEST(TIME(j.startjob), '09:00:00'),
                    LEAST(TIME(j.endjob), '18:00:00')
                ) - 
                CASE
                    WHEN TIME(j.startjob) < '12:30:00' AND TIME(j.endjob) > '13:30:00' THEN 60
                    WHEN TIME(j.startjob) < '12:30:00' AND TIME(j.endjob) > '12:30:00' THEN TIMESTAMPDIFF(MINUTE, '12:30:00', TIME(j.endjob))
                    WHEN TIME(j.startjob) < '13:30:00' AND TIME(j.endjob) > '13:30:00' THEN TIMESTAMPDIFF(MINUTE, TIME(j.startjob), '13:30:00')
                    ELSE 0
                END
            ELSE
                -- İlk gün
                TIMESTAMPDIFF(
                    MINUTE,
                    GREATEST(TIME(j.startjob), '09:00:00'),
                    '18:00:00'
                ) - 
                CASE
                    WHEN TIME(j.startjob) < '12:30:00' THEN 60
                    WHEN TIME(j.startjob) < '13:30:00' THEN TIMESTAMPDIFF(MINUTE, '12:30:00', '13:30:00')
                    ELSE 0
                END
                +
                -- Son gün
                TIMESTAMPDIFF(
                    MINUTE,
                    '09:00:00',
                    LEAST(TIME(j.endjob), '18:00:00')
                ) - 
                CASE
                    WHEN TIME(j.endjob) > '13:30:00' THEN 60
                    WHEN TIME(j.endjob) > '12:30:00' THEN TIMESTAMPDIFF(MINUTE, '12:30:00', TIME(j.endjob))
                    ELSE 0
                END
        END AS daily_work_minutes
    FROM
        job_intervals j
    JOIN
        working_days w ON j.start_date = w.date OR j.end_date = w.date
)
SELECT
    startjob,
    endjob,
    SUM(daily_work_minutes) AS total_working_minutes
FROM
    work_intervals
GROUP BY
    startjob,
    endjob;


INSERT INTO holidays (holiday_date) VALUES
('2024-01-01'),
('2024-04-23'),
('2024-05-01'),
('2024-07-18');
-- Diğer tatil günlerini ekleyin