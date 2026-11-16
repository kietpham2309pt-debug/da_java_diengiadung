(function () {
  var capNhatLoai = function () {
    var chon = document.querySelector("[data-loai-giam]:checked");
    var phanTram = !chon || chon.value === "PHAN_TRAM";
    var donVi = document.querySelector("[data-don-vi]");
    if (donVi) {
      donVi.textContent = phanTram ? "(%)" : "(₫)";
    }
    document.querySelectorAll("[data-chi-phan-tram]").forEach(function (el) {
      el.hidden = !phanTram;
    });
  };
  document.querySelectorAll("[data-loai-giam]").forEach(function (r) {
    r.addEventListener("change", capNhatLoai);
  });
  capNhatLoai();

  var capNhatPhamVi = function () {
    var chon = document.querySelector("[data-pham-vi]:checked");
    document.querySelectorAll("[data-theo]").forEach(function (el) {
      el.hidden = !chon || chon.value !== el.dataset.theo;
    });
  };
  document.querySelectorAll("[data-pham-vi]").forEach(function (r) {
    r.addEventListener("change", capNhatPhamVi);
  });
  capNhatPhamVi();

  var nutTao = document.getElementById("tao-ma");
  if (nutTao) {
    nutTao.addEventListener("click", function () {
      fetch(nutTao.dataset.url)
        .then(function (kq) { return kq.json(); })
        .then(function (d) {
          var o = document.getElementById("ma");
          o.value = d.ma;
          o.focus();
        });
    });
  }
})();
