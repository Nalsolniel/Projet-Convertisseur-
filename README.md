AMARO Thomas 21600112
CULERIER Tanguy 21601979
LAPEGUE Thibault 21600458

MANUEL UTILISATEUR

L'application se lance via le termianl grâce à la commande "java -jar" on va ensuite être invité à choisir si l'on veut effectuer une conversion d'un fichier JSON vers un fichier CSV ou bien d'un fichier CSV en fichier JSON, il faudra par la suite donner le nom du fichier à convertir. 
On va par la suite  être notifié de la création d'un fichier de configuration qui est modifiable pour par exemple réaliser la concatenation de plusieurs valeurs.
L'application va enfin demander le nom du fichier de retour et écrire à l'interieur.

MANUEL TECHNIQUE

Cette application peut prendre en entrée un fichier CSV ou JSON respectant les conditions de ses extensions. C'est à dire des valeurs séparée par des virgules pour les fichier CSV ou bien des accolades ou des crochets séparants les objet et les listes en JSON.
L'application va par la suite génerer un fichier de configuration qui sera modifiable.

Il prendra la forme suivante : 
-Les noms des objets/listes seront suivit du caractere ' : '
-Les noms des valeurs seront suivie du caractère ' < ' puis de la valeur que cette dernière prendra, cette valeur pourra prendre les formes suivantes : 
	- Un nom de valeur étant déjà présent dans le fichier de configuration dans le même objet
	- Une valeur numérique ou une chaine de caractère, dans ce cas cette valeur sera écrite entre " "
	- Un résultat d'opérations mélangeant des noms de valeur avec des valeurs numériques/chaine de caractere séparées par des opérations. Les opérations de base comme l'addition (+), la multiplication (*), la soustraction (-), la division (/) mais aussi la concatenation (|) sont possible. Les opérateurs seront écrits tels quels, sans ajout de "" ou bien de (), seulement avec le symbole précisé. Il est à noter que les opérateurs '+ - / *' prennent des valeurs numériques alors que l'opérateur '|' peut prendre des valeurs numérique ou des chaines de caractere.

Un est un seul espace devra séparer chaque nom de valeur, opérateur ou caractère '<' 

Une fois le fichier de configuration modifier on peut continuer le déroulement de l'application



Dans le cas d'une conversion d'un fichier CSV vers le format JSON :

On va en premier génerer le fichier de configuration comme suit :

On rentre dans la fonction "reader()" qui va délayer le travail à la fonction "genConfFile(String[] header)" qui prend la premiere ligne du fichier csv.

On va dans un premier temps écrire les données de profondeur 1 du csv (On entend ici que une données et de profondeur 1 si elle ne s'ecrit que par son nom, à contrario une données de profondeur supérieur est écrite comme par exemple menu_id ou menu est un objet contenant la valeur id (cet exemple montre une donnée de profondeur 2)).

On a de plus initialisé beaucoup de variable (la plupart du temps des tableaux) qui seront présentées ci-après.

Un premier tableau de booléen d'une longueur du nombre de colonne dans le csv avait initialisé à "false", et le numéro de case correspondant aux valeurs de profondeur 1 précédemment écrite on été mise à "true", ce tableau est trés important car tant que toute les cases n'ont pas été écrites dans le fichier de configuration (soit toutes les cases du tableau à "true"), alors on continue de chercher ce qu'il manque, c'est le rôle de "traitementFini(boolean[] matriceTraitee)".

Tant que le traitement n'est pas fini on va demander à "selectTab(String[] matriceATraitee,boolean[] matriceTraitee)" qui prend en premier argument la premiere ligne du fichier csv et en deuxieme le tableau de booléen expliqué plus tôt, elle va rendre en sortie un tableau de String contenant toutes les données ou "mot" commençant par le même "pere" (en reprenant l'exemple de menu_id, on prendrait tous les mots commençant par menu).

On va ensuite passer le relai à la fonction "ecrireMotPareilActualiserMatriceBoolean(String[] matriceATraitee,boolean[] matriceTraitee,FileWriter write)" qui prend les deux mêmes premiers paramètres, et en troisième le pointeur vers le fichier de configuration afin d'écrire.
Le rôle de cette fonction est d'écrire tous les mots commançant pareil en partant de celui ayant la profondeur la plus petite, c'est le rôle de "casePlusPetitElement(String[] matriceATraitee,boolean[] matriceTraitee)" de trouver ce dernier. On va par la suite écrire la profondeur de la donnée (matérialisée par le caractère '-'), et la forme qu'il va prendre, une valeur('<') ou bien un objet (':'). On va ensuite actualiser le tableau de booléen, ainsi qu'un tableau de blacklist qui permet de ne pas réécrire des données déjà écrites, c'est la fonction "peutEcrire(String[] blackList,String mot)" qui gère si le mot à le droit ou non d'être placé dans le fichier de configuration.

Le fichier de configuration à été généré.

On va maintenant pouvoir passer à la conversion.

On passe par la fonction "initialisation(String fichierRetour)", qui va, comme pour "reader()" initialiser beaucoup de tableau/variable, et appeler "traitement(...)", cette dernière est une fonction récursive et qui prend beaucoup de paramètre, par soucis de clarté il ne seront pas écrit ici mais annoté comme "(en paramètre)" dans les explications ci-dessous.
La condition d'arrêt de cette fonction récursive est si "line" (en paramètre), qui représente la ligne du fichier de configuration lue, est null (fin du fichier). On va sinon distinguer deux grands cas, si l'on à une valeur ('<'), ou un objet (':').

On va dans la suite parler de la méthode accumulate qui permet "d'écrire" des objets JSON, et prend en parametre une "key" et une "value"; 

Dans le cas d'un objet on va aussi distinguer deux cas, celui ou l'objet est une liste et ou il ne l'est pas. Dans les deux cas la fonction va appeler la methode accumulate sur son pere qui est un Objet JSON contenu dans une list. La "key" vaudra le nom écrit dans le fichier de configuration et la value l'appel récursif à cette fonction. La seul différence entre l'appel récursif sur un objet étant une liste ou non, ce fait dans le dernier argument de la fonction "pereEstListe" (en paramètre), qui est un booléen.

Dans le cas d'une valeur on va faire presque la même chose, on va distinguer le cas ou la valeur est simple, et non (une valeur est non simple si elle a éte modifié dans le fichier de configuration). De plus dans le cas ou la valeur est simple on fait une distinction entre celle dont le pere ou elle même sont des listes ou non.

On obtient à la fin un objet JSON contenant l'accumlation de tout, on l'écrit donc dans le fichier.
