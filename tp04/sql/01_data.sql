-- Cleanup
DELETE FROM customer;
DELETE FROM item;
DELETE FROM product;
DELETE FROM category;

-- Load
INSERT INTO category VALUES ('FISH', 'Fish', 'Any of numerous cold-blooded aquatic vertebrates characteristically having fins, gills, and a streamlined body' );
INSERT INTO category VALUES ('DOGS', 'Dogs', 'A domesticated carnivorous mammal related to the foxes and wolves and raised in a wide variety of breeds' );
INSERT INTO category VALUES ('REPTILES', 'Reptiles', 'Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle' );
INSERT INTO category VALUES ('CATS', 'Cats', ' Small carnivorous mammal domesticated since early times as a catcher of rats and mice and as a pet and existing in several distinctive breeds and varieties' );
INSERT INTO category VALUES ('BIRDS', 'Birds', 'Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings' );

INSERT INTO product VALUES ('FISW01', 'Angelfish', 'Saltwater fish from Australia', 'FISH');
INSERT INTO product VALUES ('FISW02', 'Tiger Shark', 'Saltwater fish from Australia', 'FISH');
INSERT INTO product VALUES ('FIFW01', 'Koi', 'Freshwater fish from Japan', 'FISH');
INSERT INTO product VALUES ('FIFW02', 'Goldfish', 'Freshwater fish from China', 'FISH');
INSERT INTO product VALUES ('K9BD01', 'Bulldog', 'Friendly dog from England', 'DOGS');
INSERT INTO product VALUES ('K9PO02', 'Poodle', 'Cute dog from France', 'DOGS');
INSERT INTO product VALUES ('K9DL01', 'Dalmation', 'Great dog for a fire station', 'DOGS');
INSERT INTO product VALUES ('K9RT01', 'Golden Retriever', 'Great family dog', 'DOGS');
INSERT INTO product VALUES ('K9RT02', 'Labrador Retriever', 'Great hunting dog', 'DOGS');
INSERT INTO product VALUES ('K9CW01', 'Chihuahua', 'Great companion dog', 'DOGS');
INSERT INTO product VALUES ('RPSN01', 'Rattlesnake', 'Doubles as a watch dog', 'REPTILES');
INSERT INTO product VALUES ('RPLI02', 'Iguana', 'Friendly green friend', 'REPTILES');
INSERT INTO product VALUES ('FLDSH01', 'Manx', 'Great for reducing mouse populations', 'CATS');
INSERT INTO product VALUES ('FLDLH02', 'Persian', 'Friendly house cat, doubles as a princess', 'CATS');
INSERT INTO product VALUES ('AVCB01', 'Amazon Parrot', 'Great companion for up to 75 years', 'BIRDS');
INSERT INTO product VALUES ('AVSB02', 'Finch', 'Great stress reliever', 'BIRDS');

INSERT INTO item  VALUES ('EST1', 'Large', '10.00', 'FISW01');
INSERT INTO item  VALUES ('EST2', 'Thootless', '10.00', 'FISW01');
INSERT INTO item  VALUES ('EST3', 'Spotted', '12.00', 'FISW02');
INSERT INTO item  VALUES ('EST4', 'Spotless', '12.00', 'FISW02');
INSERT INTO item  VALUES ('EST5', 'Male Adult', '12.00', 'FIFW01');
INSERT INTO item  VALUES ('EST6', 'Female Adult', '12.00', 'FIFW01');
INSERT INTO item  VALUES ('EST7', 'Male Puppy', '12.00', 'FIFW02');
INSERT INTO item  VALUES ('EST8', 'Female Puppy', '12.00', 'FIFW02');
INSERT INTO item  VALUES ('EST9', 'Spotless Male Puppy', '22.00', 'K9BD01');
INSERT INTO item  VALUES ('EST10', 'Spotless Female Puppy', '22.00', 'K9BD01');
INSERT INTO item  VALUES ('EST11', 'Spotted Male Puppy', '32.00', 'K9PO02');
INSERT INTO item  VALUES ('EST12', 'Spotted Female Puppy', '32.00', 'K9PO02');
INSERT INTO item  VALUES ('EST13', 'Tailed', '62.00', 'K9DL01');
INSERT INTO item  VALUES ('EST14', 'Tailless', '62.00', 'K9DL01');
INSERT INTO item  VALUES ('EST15', 'Tailed', '82.00', 'K9RT01');
INSERT INTO item  VALUES ('EST16', 'Tailless', '82.00', 'K9RT01');
INSERT INTO item  VALUES ('EST17', 'Tailed', '100.00', 'K9RT02');
INSERT INTO item  VALUES ('EST18', 'Tailless', '100.00', 'K9RT02');
INSERT INTO item  VALUES ('EST19', 'Female Adult', '100.00', 'K9CW01');
INSERT INTO item  VALUES ('EST20', 'Female Adult', '100.00', 'K9CW01');
INSERT INTO item  VALUES ('EST21', 'Female Adult', '20.00', 'RPSN01');
INSERT INTO item  VALUES ('EST22', 'Male Adult', '20.00', 'RPSN01');
INSERT INTO item  VALUES ('EST23', 'Male Adult', '120.00','FLDSH01');
INSERT INTO item  VALUES ('EST24', 'Female Adult', '120.00', 'FLDSH01');
INSERT INTO item  VALUES ('EST25', 'Male Adult', '120.00', 'AVCB01');
INSERT INTO item  VALUES ('EST26', 'Female Adult', '120.00', 'AVCB01');

INSERT INTO customer VALUES ('marc123', 'Marc', 'Fleury', '545 123 45', '65 Ritherdon Road', '', 'Los Angeles', 'LA', '56421', 'USA');
INSERT INTO customer VALUES ('bill000', 'Bill', 'Gates', '654 046 12', '27 West Side', 'Story', 'Alhabama', 'Texas', '8401', 'USA');
INSERT INTO customer VALUES ('job5', 'Steve', 'Jobs', '548 157 15', '154 Star Boulevard', '', 'San Francisco', 'WC', '5455', 'USA');
