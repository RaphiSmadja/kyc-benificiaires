-- Add Companies
INSERT INTO company (id, name) VALUES (1, 'Company A');
INSERT INTO company (id, name) VALUES (2, 'Company B');
INSERT INTO company (id, name) VALUES (3, 'Company C');

-- Add Persons
INSERT INTO person (id, name) VALUES (1, 'Mme Zoé');
INSERT INTO person (id, name) VALUES (2, 'M. Yves');

-- Add ShareHolders
INSERT INTO shareholder (id, company_id, person_id, percentage) VALUES (1, 1, 1, 10.0); -- Mme Zoé détient 10% de Société A
INSERT INTO shareholder (id, company_id, person_id, percentage) VALUES (2, 1, 2, 30.0); -- M. Yves détient 30% de Société A
INSERT INTO shareholder (id, company_id, person_id, percentage) VALUES (3, 1, 2, 50.0); -- Société B détient 50% de Société A
