# TP04 Base de données

## Expression des besoins

L'absence d'une vraie couche de persistance a été remarquée incidemment quand l'ordinateur de Bill s'est éteint ; le client réclame maintenant une base de données.

Mais par ailleurs, un autre employé de PetStore, John, est responsable de la gestion du catalogue, et a lui aussi besoin d'un logiciel.

Comme nous disposons d'une solution qui tourne pour la liste des clients, on décide de créer l'application de gestion du catalogue ; à terme, la gestion client devrait utiliser la même base de données.

Nous allons donc architecturer notre application de gestion client en ayant à l'esprit l'idée de factoriser le plus possible de code.

La société YAPS est satisfaite du logiciel de gestion des clients Petstore Customer. Elle voudrait maintenant informatiser la gestion de son **catalogue** d'animaux de compagnie. Le catalogue est divisé en **catégories**. Pour l'instant YAPS ne gère que cinq catégories d'animaux, bien qu'elle envisage d'étendre sa gamme : *Poissons Chiens Chats Reptiles Oiseaux*.

Chaque catégorie est sub-divisée en **produit**. Par exemple pour les chiens, on peut avoir les produits 
suivants : *Bulldog Caniche Dalmatien Labrador Lévrier*.

Puis enfin chaque produit est, à son tour, divisé en **article**. Ce sont ces articles qui sont proposés et vendus aux clients. Par exemple, le produit Caniche regroupe les articles finaux suivants :
*Caniche femelle adulte ; Caniche mâle adulte; Caniche femelle 3 mois ; Caniche mâle 3 mois*

Cette gestion du catalogue sera effectuée par John. L'application sera déployée sur son poste comme client textuel ; en revanche, elle utilisera une base de donnée hebergée sur un serveur. L'application se nomme **Petstore Catalog**.

## Vue Utilisateur

### Diagramme de cas d'utilisation

~~~plantuml
@startuml
title Figure 1 - Diagramme de cas d'utilisation de la gestion du catalogue

package catalog {
 :John: --> (gérer les catégories)
 :John: --> (gérer les produits)
 :John: --> (gérer les articles) 
}
@enduml
~~~

Les *use case* sont généralement assez précis ; cependant, comme notre programme (dans son état actuel) est essentiellement du *CRUD*, nous avons simplifié le schéma. « géré » sous-entend en fait :

- créer une entrée
- supprimer une entrée ;
- mettre à jour une entrée ;
- chercher une entrée.

### Cas d'utilisation « Gérer les catégories »


#### Nom

Gérer les catégories

#### Résumé 
Permet à John de créer/modifier/supprimer/lister les catégories du catalogue.

#### Acteurs
John.
  
#### Pré-conditions
Aucune

#### Description
YAPS veut saisir les catégories de son catalogue et les persister. Elle pourra ensuite, si elle le désire, en modifier le contenu, supprimer certaines catégories, en afficher une ou la liste entière. Une catégorie contient les données suivantes :

- Category Id : identifiant unique de la catégorie ((1))((2)). Cet identifiant est construit manuellement par YAPS.
- Name : nom de la catégorie (ex. Poisson, Chien, Chat...)
- Description de la catégorie : (ex. Un chien est un animal affectueux qui partagera avec vous des moments de bonheur) Une catégorie possède aussi une liste de produits.
Les champs Category Id, Name et Description sont obligatoires ((2)).

#### Exceptions
- ((1)) Si l'identifiant saisi existe déjà dans le système, une exception DuplicateKeyException est levée.
- ((2)) Une exception est levée si l'un des champs est manquant.
- Si une erreur inattendue se produit, le logiciel doit s'arrêter ; le *stacktrace* de Java sera copié et collé et envoyé aux développeur pour analyse.

(dans un deuxième temps, on envisagera de détecter les problème de disponibilité de la base).

#### Post-conditions
Une catégorie est créée/modifiée/supprimée/listée.

#### Remarque

Ce *use-case* est assez loin de ce qu'on fait normalement (et de ce qu'on vous demandera en **GLG 204**). En particulier, préciser à ce niveau les exceptions demandées relève de la conception du code et non de l'expression des besoins.

### Cas d'utilisation « Gérer les produits »

#### Nom
Gérer les produits.

#### Résumé
Permet à John de créer/modifier/supprimer/lister les produits du catalogue.

#### Acteurs
John.

#### Pré-conditions
- Pour toute manipulation d'un produit, la catégorie à laquelle se rapporte le produit doit exister dans le système ;
- Il doit y avoir au moins une catégorie dans le catalogue pour pouvoir ajouter un produit.

#### Description

YAPS veut saisir les produits de son catalogue, les rattacher à une catégorie et les persister. Elle pourra ensuite, si elle le désire, en modifier le contenu, supprimer certains produits, en afficher un ou la liste entière. 

Un produit contient les données suivantes :

- Product Id : identifiant unique du produit ((1))((2)). Cet identifiant est construit manuellement par YAPS.
- Name : nom du produit (ex. Bulldog, Caniche, Dalmatien...)
- Description du produit : (ex. Un caniche est un petit chien affectueux qui ne prendra pas trop de place et saura vous réconforter de sa tendresse)

Lorsqu'on affiche les données d'un produit, on veut aussi visualiser l'identifiant et le nom de la catégorie à laquelle se rattache le produit.

Les champs Product Id, Name et Description sont obligatoires ((2)).

#### Exceptions

- ((1)) Si l'identifiant saisi existe déjà dans le système, une exception DuplicateKeyException est levée.
- ((2)) Une exception est levée si l'un des champs est manquant.

**à revoir**
#### Post-conditions
Un produit est créé/modifié/supprimé/listé.

### Cas d'utilisation « Gérer les articles »

#### Nom
Gérer les articles.

#### Résumé
Permet à John de créer/modifier/supprimer/lister les articles du catalogue.

#### Acteurs
John.

#### Pré-conditions
- Pour la création/modification d'un article, le produit auquel se rapporte l'article doit exister dans le système.
- - Il doit y avoir au moins un produit dans le catalogue pour pouvoir ajouter un article.
#### Description

YAPS veut saisir les articles de son catalogue, les rattacher à un produit et les persister. Elle pourra ensuite, si elle le désire, en modifier le contenu, supprimer certains articles, en afficher un ou la liste entière. Un article contient les données suivantes :
- Item Id : identifiant unique de l'article ((1))((2)). Cet identifiant est construit manuellement par YAPS.
- Name : nom de l'article (ex. Caniche 3 mois femelle...)
- Unit Cost : prix unitaire de l'article

Lorsqu'on affiche les données d'un article, on veut aussi visualiser l'identifiant et le nom du produit auquel se rattache l'article.
- Les champs Item Id, Name et UnitCost sont obligatoires ((2)).

#### Exceptions
- ((1)) Si l'identifiant saisi existe déjà dans le système, une exception DuplicateKeyException est levée.
- ((2)) Une exception est levée si l'un des champs est manquant.

#### Post-conditions
Un article est créé/modifié/supprimé/listé.
## Écrans


Les actions sont identiques à l'application de Bill, cependant il existe 3 menus supplémentaires : 

- la possibilité d'obtenir la liste complète des catégories, des produits et des articles ;

- Lors de la consultation d'un produit, l'application affichera l'identifiant et le nom de la catégorie auquel il est rattaché ;

## Analyse et conception
### Vue logique

Les objets métiers constituant le catalogue de YAPS sont les catégories (Category), les produits (Product) et les articles (Item). Ils sont liés entre eux par des liens de composition.


~~~plantuml 
@startuml
title Figure 2 - Diagramme de classes du catalogue

class Category {  
    id : String;  
    name : String
    description : String    
}

class Product {
    id : String;  
    name: String
    description: String
}

class Item {
    id : String;  
    name: String 
    unitCost: double
}

Category "1" *- "*" Product
Product "1" *- "*" Item
@enduml
~~~

Vous pouvez interpréter le diagramme ci-dessus de la manière suivante :

- Une catégorie peut avoir zéro ou plusieurs produits, et un produit peut avoir zéro ou plusieurs articles.
- Les relations de composition nous indiquent que la suppression d'une catégorie entraînera la suppression de ses produits liés.
- Le choix d'une relation uni ou bi-directionnelle relève d'un choix de **conception**. Nous nous en occuperons plus tard, en remarquant cependant que les liens bi-directionnels sont plus complexes à gérer.


## Analyse et conception

*Note sur cette partie :*  le découpage *analyse et conception* est ici assez illusoire, dans la mesure où le choix d'une base de données est un choix technique, et relève déjà de la conception.


### Vue logique

La livraison de cette nouvelle version de PetStore nous 
permet de faire quelques 
remaniements au niveau des objets métier. 
Comme on peut le voir sur le diagramme de classe de 
la figure 2, l'attribut identifiant (`id`), présent dans chaque classe
métier précédemment, 
a été déplacé dans la super-classe `DomainObject`.

> Les classes du domaine possèdent toutes un identifiant (id). Le remaniement Extraire Super Classe (**Extract Superclass**) permet de créer des classes mères et d'y déplacer le code des classes filles.

~~~plantuml
@startuml
title Figure 2 - Diagramme de classes des objets métiers
class DomainObject {
    id : String
}

class Category << entity >>  {
    name : String
    description: String    
}

class Product << entity >> {
    name: String
    description: String
}
class Item << entity >> {
    name: String
    unitCost: double
}

DomainObject <|-- Category
DomainObject <|-- Product
DomainObject <|-- Item

Product -> Category : category
Item -> Product : product

@enduml
~~~

On choisit ici d'utiliser des liens **unidirectionnels** entre `Product` et `Category` et entre `Item` et `Product`. C'est plus simple à gérer. Si jamais on voulait lister tous les produits d'une catégorie donnée, il serait toujours possible de le demander à la couche de persistance.


Pour concevoir la couche de persistance, nous allons utiliser le design pattern Data Access Object (**DAO**).

> Le design pattern Data Access Object permet de centraliser dans un objet dédié le lien entre la couche d'accès aux données et la couche métier (constituée alors de POJOs).
> Le DAO peut utiliser une base de données, un fichier texte, une base objet ou même un serveur LDAP. Les applications PetStore Customer et Catalog persisteront leurs données dans une base de données relationnelle uniquement.

Ci-dessous, le diagramme de classes représentant 
les liens entre les objets métiers (`Customer`, `Category`, 
`Product` et `Item`)
et leurs DAO respectifs (`CustomerDAO`, `CategoryDAO`, `ProductDAO` et
`ItemDAO`). La superclasse `AbstractDataAccessObject` possède aussi
une relation d'utilisation avec la superclasse DomainObject.

~~~plantuml
@startuml
title Figure 3 - Diagramme de classe représentant les liens entre objets du domaine et DAO
skinparam linetype ortho

AbstractDAO .> DomainObject
class CustomerDAO <<lifecycle>> {}
class Customer <<entity>> {}

CustomerDAO .> Customer
AbstractDAO <|-- CustomerDAO
DomainObject <|-- Customer

class CategoryDAO <<lifecycle>> {}
class Category <<entity>> {}
CategoryDAO .> Category
AbstractDAO <|-- CategoryDAO
DomainObject <|-- Category

class ProductDAO <<lifecycle>> {}
class Product <<entity>> {}
ProductDAO .> Product
AbstractDAO <|-- ProductDAO
DomainObject <|-- Product

class ItemDAO <<lifecycle>> {}
class Item <<entity>> {}
ItemDAO .> Item
AbstractDAO <|-- ItemDAO
DomainObject <|-- Item


@enduml
~~~

Le code SQL est présent uniquement  dans les DAO, que ce soient des classes  concrètes (`CustomerDAO`, `CategoryDAO`, ...)
ou leur superclasse `AbstractDAO`.

### Vue Processus

Le diagramme de séquence ci-dessous liste les différents appels aux objets pour pouvoir afficher un produit (Product). Le problème principal qu'on va rencontrer est qu'un produit est lié à une catégorie. Quand on le sauvegarde ou qu'on le recharge, il faut donc aussi sauver/recharger celle-ci. Les bases de données relationnelles ne connaissent quant à elles que les identifiants des objets.

La question de savoir *qui* rétablit les liens entre les objets n'a pas vraiment de bonne réponse. Nous décidons que, comme `ProductDAO` est supposé nous renvoyer un `Product`, autant que ce soit lui qui aille récupérer la catégorie du produit.

Cela entraîne que `ProductDAO` va utiliser `CategoryDAO`.

~~~plantuml
@startuml
title Figure 4 - Diagramme de séquence pour afficher un produit et sa catégorie liée
boundary CatalogApplication as ui
entity "p : Product" as p
entity "c : Category" as c

create ProductDAO
ui -> ProductDAO : new
ui -> ProductDAO ++ : select()


create CategoryDAO
ProductDAO -> CategoryDAO : new
ProductDAO -> CategoryDAO ++ : select()
create c
CategoryDAO -> c : new
CategoryDAO -> ProductDAO -- : c
create p
ProductDAO -> p : new
ProductDAO -> p : setCategory(c)
ProductDAO -> ui -- : p 
ui -> p : toString()
@enduml
~~~



~~~plantuml
@startuml
title Figure 5 - Diagramme de séquence pour créer un produit
boundary CatalogApplication as ui
entity "p : Product" as p
participant ProductDAO
participant CategoryDAO
entity "c : Category" as c

create p
create CategoryDAO
ui -> CategoryDAO : new
ui -> CategoryDAO : findById()

create c
CategoryDAO -> c : new

ui -> Product : setCategory()
create ProductDAO
ui -> ProductDAO : new
ui -> ProductDAO : save()
@enduml
~~~



## Vue implementation

L'application PetStore continue à augmenter son nombre
de classes. En réutilisant les remaniements 
« **Extraire Paquetage** » et « **Déplacer Classe** »
on se retrouve avec la structure suivante :

~~~plantuml
@startuml
title Figure 6 - Paquetages de l'application
'left to right direction
top to bottom direction

package ui {
    class CatalogApplication
}

package domain {
    class DomainObject

    domain.category -[hidden]> DomainObject

    package domain.category {
        class Category
        class CategoryDAO
    }
    package domain.product {
        class Product
        class ProductDAO
    }
    domain.category --[hidden]> domain.product

    package domain.item {
        class Item
        class ItemDAO
    }
}

ui --[hidden]> domain


package persistence {
    class AbstractDAO
    package template {
        class SQLSimpleHelper
    }
}


@enduml
~~~

Puiqu'à présent nous utilisons une base de données, il est plus approprié de nommer la méthode de recherche findById au lieu de tout juste find. Pour cela nous utilisons le remaniement Renommer Méthode (**Rename Method**).


## Architecture

Le diagramme de composants ci-dessous nous montre les
trois grands sous-systèmes de l'application : 
l'interface utilisateur (composée de `CatalogApplication` qui dialogue avec les objets du domaine qui, eux-mêmes, délèguent la persistance à une couche de DAO puis à la persistance en tant que telle, ici un SGBDR.

~~~plantuml
@startuml
package ui

package domain {
    package dao {

    }
    package model {

    }
}


ui --> dao
ui --> model
dao -> model
dao --> baseDeDonnees : JDBC


@enduml
~~~

*Figure 7 - Diagramme de composants montrant les sous-systèmes Schéma de la base de données*

### Schéma de base de données

Chaque classe métier devra persister ses données dans
une table relationnelle.

On aura donc les tables :

~~~sql
CREATE TABLE category(
    id VARCHAR(10),
    PRIMARY KEY(id), 
    name VARCHAR(50) NOT NULL, 
    description VARCHAR(255) NOT NULL
) ENGINE=INNODB ;

CREATE TABLE product(
    id VARCHAR(10), 
    PRIMARY KEY(id), 
    name VARCHAR(50) NOT NULL, 
    description VARCHAR(255) NOT NULL,
    category_fk VARCHAR(10) NOT NULL, 
    INDEX category_fk_ind (category_fk),
    FOREIGN KEY (category_fk) REFERENCES category(id) ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE item(
    id VARCHAR(10),
    PRIMARY KEY(id), 
    name VARCHAR(50) NOT NULL,
    unit_cost DOUBLE NOT NULL,
    product_fk VARCHAR(10) NOT NULL, 
    INDEX product_fk_ind (product_fk), 
    FOREIGN KEY (product_fk) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=INNODB;
~~~

*Figure 8 - Schéma de la base de données*

Remarquez la présence de clés étrangères. La relation 1..n entre les catégories et les produits se fait dans la table fille, c'est-à-dire que la table `product` possède une clé `category_fk` pointant vers la table `category`. Idem pour les tables `product` et `item`.

Notez que nous avons choisi de nous simplifier la vie en n'utilisant que des **minuscules** dans les noms des tables et des champs SQL. **MySQL** est en effet très capricieux à ce sujet, et se comporte différemment selon les systèmes de fichiers qui l'hébergent.




### Vue déploiement

Les applications *PetStore Customer* et *PetStore Catalog*, respectivement déployées sur les postes de Bill et John, utiliseront toutes deux la base de données distante **MySQL** via JDBC.

~~~plantuml
@startuml
title Figure 9 - Diagramme de déploiement des deux postes et de la base de données
node "PC de Bill" <<device>> as bill {
    node JVM <<ExecutionEnvironment>> as jvm1 {
        artifact clientCustomer.jar as c1
    }
}

node "PC de John" <<device>> as john {
 node JVM <<ExecutionEnvironment>> as jvm2  {
        artifact clientCatalogue.jar as c2
  }
} 

node "Serveur SQL" {
    artifact mysql
}

c1 --> mysql : jdbc
c2 --> mysql : jdbc

@enduml
~~~


Comme dans la version précédente,
l'application est déployée dans un fichier .jar qui  se trouve sous le répertoire `build/`. Pour que les applications clientes puissent fonctionner, elles doivent avoir les drivers JDBC de MySQL dans leur classpath.


### Implémentation


Après la création d'une première version de `CategoryDAO`, on s'aperçoit que le code sera très répétitif. On décide donc de factoriser (un peu) le code, ce qui conduit à l'écriture de la classe `AbstractDAO`


 On décide donc de factoriser ce qui peut l'être. Nous développons ainsi un mini-framework pour la gestion des DAO, qui se trouve dans le package `com.yaps.utils.dao`. Le code de certaines méthodes peut même être écrit indépendamment de la classe de l'objet métier concerné par la DAO ; c'est en particulier le cas de la méthode `remove`.

L'architecture obtenue pour les DAO est la suivante :

~~~plantuml
@startuml
abstract class AbstractDAO<T> {
    # getConnection() : Connection
    # getTableName(): String
    # getFieldsNames(): String[]
    {abstract} + save(o: T)
    {abstract} + update(o: T)
    {abstract} + findAll() : List<T>
    {abstract} + findById(id: String) : Optional<T>
    + remove(id: String)
}


class CategoryDAO {
    + save(o: Category)
    + update(o: Category)
    + findAll() : List<Category>
    + findById(id: String) : Optional<Category>
}


class ProductDAO {
    + save(o: Product)
    + update(o: Product)
    + findAll() : List<Product>
    + findById(id: String) : Optional<Product>
}

class ItemDAO {
    + save(o: Item)
    + update(o: Item)
    + findAll() : List<Item>
    + findById(id: String) : Optional<Item>
}
AbstractDAO <|-- CategoryDAO
AbstractDAO <|-- ProductDAO
AbstractDAO <|-- ItemDAO

@enduml
~~~

Notez que le code de la méthode `remove` est directement écrit dans `AbstractDAO` et hérité par les implémentations.

Pour faciliter l'implémentation des recherches, on définit aussi une interface fonctionnelle `ResultSetExtractor<T>`, dotée d'une seule méthode, `extract`, qui sait récupérer un objet à partir d'une ligne d'un `ResultSet`. La classe `CategoryDAO`, qui vous est donnée en exemple, vous montrera comment elle est utilisée.

~~~plantuml
@startuml
interface ResultSetExtractor<T> {
    extract(ResultSet res) : T
}
@enduml
~~~

Petite note technique : remarquez le type générique dans la déclaration de `AbstractDAO` :

~~~java
abstract class AbstractDAO<T extends DomainObject> {
...
}
~~~

En précisant que le type `T` étend `DomainObject`, on se donne le droit d'utiliser les diverses méthodes de `DomainObject` dans la DAO, comme par exemple la méthode `getId()`. C'est ce qui permet entre autres d'écrire la méthode `remove()` dans `AbstractDAO`.

#### votre travail
Vous pouvez maintenant développer l'application à partir de la version précédente, ou télécharger la liste des classes fournies pour commencer votre développement. 

Les hiérarchies de classe `DomainObject`, `AbstractDAO` et `CategoryDAO` vous sont données pour vous permettre de reproduire la même technique de développement pour les autres classes métiers.

On vous demande d'écrire les classes :

- Item
- ItemDAO
- Category
- CategoryDAO

Attention, notez bien que les méthodes d'extractions des DAO doivent renvoyer un objet complet. Un `Product`, par exemple, doit avoir sa `Category`. Le  `ProductDAO` doit donc aller chercher la catégorie du produit. C'est en fait assez simple à implémenter :

- vous avez accès, dans `ProductDAO`, à la connexion actuelle ;
- vous pouvez donc créer une `CategorieDAO` pour aller chercher la catégorie voulue.

Les diagrammes de séquence devraient vous aider.

On utilise dans l'interface utilisateur, les méthodes `toString` et `shortDisplay` pour donner une visualisation
des entrées. Pensez bien à les écrire (les tests vous guideront).

### Conseils pour compiler et lancer l'application

Pour compiler l'application, il faut que mysql tourne dans un environnement docker.

Pour cela, la commande serait :

~~~bash
docker compose -p tp4-test -f docker-compose-test-db.yml up
~~~

Le « `-p tp-test4` »  permet de nommer le conteneur. Sinon, il prend comme nom le nom du dossier du tp, et cela vous empêche d'avoir simultanément un serveur de test et un serveur de développement.

De même, avant de lancer le programme, faites 
~~~bash
docker compose -p tp4 up
~~~

Pour vous débarrasser des conteneurs :

~~~bash
docker compose -p tp4-test down
docker compose -p tp4 down
~~~

(en cas de problème, voir la documentation de docker pour supprimer des conteneurs à la main).

Si vous désirez vous passer de docker, vous pouvez modifier les ports dans `PetstoreConnectionFactory` (ça n'est pas grave, car nous utiliserons la classe d'origine pour nos tests).

## Limitations

- l'interface utilisateur prend actuellement des décisions métier ; on pourrait introduire une couche supplémentaire qui pourrait par exemple décider de la  possibilité de créer ou non des produits ;
- on vérifie correctement avant chaque action qu'elle est possible, mais nous supposons que les données sont uniquement manipulée par le programme lancé par l'utilisateur. Elles peuvent devenir obsolètes suite à des accès concurrents. Actuellement, cela se traduit par des exceptions au niveau du runtime, et par l'arrêt du logiciel. On devrait pouvoir mieux faire.

## Références

- JDBC Technology http://java.sun.com/products/jdbc/ (http://java.sun.com/products/jdbc/)
- JDBC FAQ Home Page http://www.jguru.com/faq/JDBC (http://www.jguru.com/faq/JDBC)
- JDBC drivers in the wild http://www.javaworld.com/javaworld/jw-07-2000/jw-0707-jdbc.html (http://www.javaworld.com/javaworld/jw-07-2000/jw-0707-jdbc.html)
- Database Programming with JDBC and Java George Reese. O'Reilly. 2000.
- SQL Tutorial http://www.w3schools.com/sql/default.asp (http://www.w3schools.com/sql/default.asp)
- MySQL Cookbook Paul DuBois. O'Reilly. 2002.
- MySQL Home Page http://www.mysql.com/ (http://www.mysql.com/)
- Refactoring : Extract Superclass http://www.refactoring.com/catalog/extractSuperclass.html (http://www.refactoring.com/catalog/extractSuperclass.html)
- Refactoring : Rename Method http://www.refactoring.com/catalog/renameMethod.html (http://www.refactoring.com/catalog/renameMethod.html)
- Core J2EE Patterns - Data Access Object http://www.oracle.com/technetwork/java/dataaccessobject-138824.html (http://www.oracle.com/technetwork/java/dataaccessobject-138824.html)
- Tutoriel d'utilisation de l'API de logging java.util.logging
http://www.programmez.com/tutoriels.php?tutoriel=79&titre=Logging-dans-Java (http://www.programmez.com/tutoriels.php?tutoriel=79&titre=Logging-dans-Java)
