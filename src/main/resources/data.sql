-- Add Companies
INSERT INTO company (name) VALUES ('Company A'); -- Société A
INSERT INTO company (name) VALUES ('Company B'); -- Société B
INSERT INTO company (name) VALUES ('Company C'); -- Société C

-- Add Persons
INSERT INTO person (name) VALUES ('Mme Zoé'); -- Mme Zoé
INSERT INTO person (name) VALUES ('M. Yves'); -- M. Yves
INSERT INTO person (name) VALUES ('M. Raphael'); -- M. Raphael

-- Add Shareholders (Person as Shareholder)
INSERT INTO shareholder (company_id, person_id, shareholder_company_id, percentage)
VALUES (1, 1, NULL, 10.0); -- Mme Zoé détient 10% de Société A
INSERT INTO shareholder (company_id, person_id, shareholder_company_id, percentage)
VALUES (1, 2, NULL, 30.0); -- M. Yves détient 30% de Société A
INSERT INTO shareholder (company_id, person_id, shareholder_company_id, percentage)
VALUES (3, 3, NULL, 55.0); -- M. Raphael détient 55% de Société C

-- Add Shareholders (Company as Shareholder)
INSERT INTO shareholder (company_id, person_id, shareholder_company_id, percentage)
VALUES (1, NULL, 2, 20.0); -- Société B détient 20% de Société A
INSERT INTO shareholder (company_id, person_id, shareholder_company_id, percentage)
VALUES (2, NULL, 3, 30.0); -- Société C détient 30% de Société B
