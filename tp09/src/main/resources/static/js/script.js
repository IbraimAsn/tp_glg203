
// cache les variables javascripts pour éviter de poluer le reste du code.
(function () {

    // Boucle sur les menus déroulants pour les activer.
    // Problème avec htmlunit :
    // en utilisant  "for (const dropdown of.." qui est une notation
    // parfaitement standard, htmlunit signale une erreur de syntaxe 
    // (sans donner d'ailleurs de détail : il dit "syntax error at line 13"
    // En revanche, en mettant let... ça fonctionne.
    // à creuser.
    // 
    for (let dropdown of document.querySelectorAll('.dropdown')) {
        dropdown.addEventListener('click', function (event) {
            event.stopPropagation();
            dropdown.classList.toggle('is-active');
        });
    }

    // Même chose gérer l'adaptabilité aux supports :
    document.addEventListener('DOMContentLoaded', () => {

        // Get all "navbar-burger" elements
        const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);
      
        // Add a click event on each of them
        $navbarBurgers.forEach( el => {
          el.addEventListener('click', () => {
      
            // Get the target from the "data-target" attribute
            const target = el.dataset.target;
            const $target = document.getElementById(target);
      
            // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
            el.classList.toggle('is-active');
            $target.classList.toggle('is-active');
      
          });
        });
      
      });
})();


