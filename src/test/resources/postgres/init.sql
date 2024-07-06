CREATE TABLE "product"
(
    "product_id"  uuid PRIMARY KEY,
    "store_id"    uuid NOT NULL,
    "name"        text NOT NULL,
    "description" text,
    "image_path"  text
);

CREATE TABLE "offer"
(
    "offer_id"    uuid PRIMARY KEY,
    "store_id"    uuid            NOT NULL,
    "name"        text            NOT NULL,
    "product_id"  uuid            NOT NULL,
    "category_id" uuid            NOT NULL,
    "price"       double precision NOT NULL,
    "status"      text            NOT NULL
);

CREATE TABLE "customization"
(
    "customization_id" uuid,
    "offer_id"         uuid NOT NULL,
    "store_id"         uuid NOT NULL,
    "option_id"        uuid,
    "name"             text NOT NULL,
    "description"      text,
    "min_permitted"    int  NOT NULL,
    "max_permitted"    int  NOT NULL,
    "status"           text NOT NULL,
    PRIMARY KEY ("customization_id", "offer_id")
);

CREATE TABLE "option"
(
    "option_id"        uuid,
    "offer_id"         uuid            NOT NULL,
    "store_id"         uuid            NOT NULL,
    "product_id"       uuid            NOT NULL,
    "customization_id" uuid            NOT NULL,
    "min_permitted"    int             NOT NULL,
    "max_permitted"    int             NOT NULL,
    "price"            double precision NOT NULL,
    "status"           text            NOT NULL,
    PRIMARY KEY ("option_id", "offer_id")
);

CREATE TABLE "category"
(
    "category_id" uuid PRIMARY KEY,
    "store_id"    uuid NOT NULL,
    "name"        text NOT NULL,
    "description" text,
    "status"      text NOT NULL
);

CREATE UNIQUE INDEX ON "product" ("product_id");

CREATE INDEX ON "product" ("store_id");

CREATE UNIQUE INDEX ON "offer" ("offer_id");

CREATE INDEX ON "offer" ("store_id");

CREATE INDEX ON "customization" ("customization_id");

CREATE INDEX ON "customization" ("store_id");

CREATE INDEX ON "customization" ("offer_id");

CREATE INDEX ON "option" ("option_id");

CREATE INDEX ON "option" ("store_id");

CREATE INDEX ON "option" ("offer_id");

CREATE UNIQUE INDEX ON "category" ("category_id");

CREATE INDEX ON "category" ("store_id");

ALTER TABLE "offer"
    ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE "offer"
    ADD FOREIGN KEY ("category_id") REFERENCES "category" ("category_id") ON DELETE CASCADE;

ALTER TABLE "customization"
    ADD FOREIGN KEY ("offer_id") REFERENCES "offer" ("offer_id") ON DELETE CASCADE;

ALTER TABLE "customization"
    ADD FOREIGN KEY ("option_id", "offer_id") REFERENCES "option" ("option_id", "offer_id") ON DELETE CASCADE;

ALTER TABLE "option"
    ADD FOREIGN KEY ("offer_id") REFERENCES "offer" ("offer_id") ON DELETE CASCADE;

ALTER TABLE "option"
    ADD FOREIGN KEY ("product_id") REFERENCES "product" ("product_id");

ALTER TABLE "option"
    ADD FOREIGN KEY ("customization_id", "offer_id") REFERENCES "customization" ("customization_id", "offer_id") ON DELETE CASCADE;
