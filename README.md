Une simple API REST, developée en utilisant Framework Play Java, permettant d'une part de minifier une URL passée en paramètre et d'autre part de récupérer l'URL complète depuis l'URL raccourcie.

Cette API offre trois fonctionnalités :
* Lister les URLs minifiées

* Minifier une URL

* Récupérer l'URL complète à partir d'une URL minifiée


Ce projet est basé sur <https://github.com/boldradius/rest-java-play-sample>

---

# __1. Lister les URLs minifiées__

* Affichier la liste des correspondances Urls minifiées/complètes
	
	```curl -vX GET http://localhost:9000/url```


---

# __2. Minifier une URL__

* Minifier l'URL __*https://www.linkedin.com/in/maalejahmed*__

   ```curl -vX POST http://localhost:9000/url -d '{"url":"www.linkedin.com/in/maalejahmed"}' Type: application/json"```

* Retour: ```{"url":"JYw4IEZv"}``` (cas d'exécution)

---

# __3. Récupérer une URL__

* Récupérer l'URL __*https://www.linkedin.com/in/maalejahmed*__

   ```curl -vX GET http://localhost:9000/url/JYw4IEZv```
* La méthode retournera le code HTTP __*403 Forbidden*__ si l'URL na pas été trouvée.
* La méthode retournera l'URl d'origine si l'URL a été trouvée.
    ```{"url":"https://www.linkedin.com/in/maalejahmed"}```
