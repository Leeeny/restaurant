DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'menu') THEN
            CREATE DATABASE menu;
        END IF;
    END
$$;