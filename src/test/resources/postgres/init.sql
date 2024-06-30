CREATE TABLE "product"
(
    "id"          uuid PRIMARY KEY,
    "store_id"    uuid NOT NULL,
    "name"        text NOT NULL,
    "description" text,
    "image_path"  text
);

CREATE TABLE "offer"
(
    "id"          uuid PRIMARY KEY,
    "store_id"    uuid             NOT NULL,
    "product_id"  uuid,
    "category_id" uuid,
    "price"       double precision NOT NULL,
    "status"      text             NOT NULL
);

CREATE TABLE "customization"
(
    "id"            uuid PRIMARY KEY,
    "store_id"      uuid NOT NULL,
    "offer_id"      uuid NOT NULL,
    "option_id"     uuid,
    "name"          text NOT NULL,
    "description"   text,
    "min_permitted" int  NOT NULL,
    "max_permitted" int  NOT NULL,
    "status"        text NOT NULL
);

CREATE TABLE "option"
(
    "id"               uuid PRIMARY KEY,
    "store_id"         uuid             NOT NULL,
    "offer_id"         uuid             NOT NULL,
    "product_id"       uuid,
    "customization_id" uuid,
    "min_permitted"    int              NOT NULL,
    "max_permitted"    int              NOT NULL,
    "price"            double precision NOT NULL,
    "status"           text             NOT NULL
);

CREATE TABLE "category"
(
    "id"          uuid PRIMARY KEY,
    "store_id"    uuid NOT NULL,
    "name"        text NOT NULL,
    "description" text,
    "status"      text NOT NULL
);

CREATE INDEX ON "product" ("store_id");

CREATE UNIQUE INDEX ON "offer" ("id");

CREATE INDEX ON "offer" ("store_id");

CREATE UNIQUE INDEX ON "customization" ("id");

CREATE INDEX ON "customization" ("store_id");

CREATE INDEX ON "customization" ("offer_id");

CREATE UNIQUE INDEX ON "option" ("id");

CREATE INDEX ON "option" ("store_id");

CREATE INDEX ON "option" ("offer_id");

CREATE UNIQUE INDEX ON "category" ("id");

CREATE INDEX ON "category" ("store_id");

ALTER TABLE "offer"
    ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "offer"
    ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");

ALTER TABLE "customization"
    ADD FOREIGN KEY ("offer_id") REFERENCES "offer" ("id");

ALTER TABLE "customization"
    ADD FOREIGN KEY ("option_id") REFERENCES "option" ("id");

ALTER TABLE "option"
    ADD FOREIGN KEY ("offer_id") REFERENCES "offer" ("id");

ALTER TABLE "option"
    ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "option"
    ADD FOREIGN KEY ("customization_id") REFERENCES "customization" ("id");
