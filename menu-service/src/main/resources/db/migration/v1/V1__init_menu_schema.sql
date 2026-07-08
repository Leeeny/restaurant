CREATE SEQUENCE menu.categories_id_seq START WITH 1 INCREMENT by 50;

CREATE TABLE menu.categories
(
    id BIGINT PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE
);

CREATE SEQUENCE menu.menu_items_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE menu.menu_items
(
    id        BIGINT PRIMARY KEY,
    name              VARCHAR(128) UNIQUE NOT NULL,
    description       TEXT                NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    price             NUMERIC(10, 2)      NOT NULL CHECK (price > 0),
    category_id       BIGINT              NOT NULL REFERENCES menu.categories (id),
    cook_time_minutes INTEGER             NOT NULL CHECK (cook_time_minutes > 0),
    weight_grams      NUMERIC(6, 2)       NOT NULL CHECK (weight_grams > 0),
    image_url         TEXT,
    created_at        TIMESTAMPTZ         NOT NULL DEFAULT now(),
    updated_at        TIMESTAMPTZ         NOT NULL DEFAULT now()
);

CREATE SEQUENCE menu.ingredients_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE menu.ingredients
(
    id                BIGINT PRIMARY KEY,
    name              VARCHAR(128) UNIQUE NOT NULL,
    calories_per_100g NUMERIC(6, 2)       NOT NULL CHECK (calories_per_100g >= 0)
);

CREATE TABLE menu.menu_item_ingredients
(
    menu_item_id  BIGINT        NOT NULL REFERENCES menu.menu_items (id),
    ingredient_id BIGINT        NOT NULL REFERENCES menu.ingredients (id),
    weight_grams  NUMERIC(6, 2) NOT NULL CHECK (weight_grams > 0),

    PRIMARY KEY (menu_item_id, ingredient_id)
);

CREATE INDEX idx_menu_items_category_id ON menu.menu_items (category_id);
CREATE INDEX idx_menu_item_ingredients_ingredient_id ON menu.menu_item_ingredients (ingredient_id);