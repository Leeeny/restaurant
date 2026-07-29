DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'review') THEN
            CREATE DATABASE orders;
        END IF;
    END
$$;