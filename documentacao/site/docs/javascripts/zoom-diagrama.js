document.addEventListener("click", function (evento) {
  var diagrama = evento.target.closest(".mermaid");
  if (diagrama) {
    diagrama.classList.toggle("zoom-ativo");
    document.body.classList.toggle("zoom-ativo-fundo", diagrama.classList.contains("zoom-ativo"));
  }
});
