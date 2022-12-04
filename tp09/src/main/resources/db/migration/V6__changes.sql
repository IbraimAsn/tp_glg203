
-- changes made in version 7 of the application

-- customer has a new email field.

alter table customer add column email varchar(255) default '';

-- fix all mails to ''...
update customer set email = '';

-- note : normally, we should also add image_path to item here.
-- however, the subsequent "update" to individual items would be 
-- really annoying to write. Hence, we cheat and modify everything in the original files.

