(function () {
  var dinhDang = function (so) {
    return so.toLocaleString("vi-VN").replace(/,/g, ".") + " ₫";
  };

  var khung = document.querySelector(".tt[data-tam-tinh]");
  if (khung) {
    var form = document.getElementById("form-dat-hang");
    var tamTinh = Number(khung.dataset.tamTinh);
    var giamCk = Number(khung.dataset.giamCk);
    var chonTinh = document.getElementById("tinhThanh");
    var chonPhuong = document.getElementById("phuongXa");
    var hopUuDai = document.getElementById("tt-uu-dai");
    var hoi = document.getElementById("tt-hoi");
    var daHoi = false;
    var duLieuDiaChi = null;

    var napDiaChi = function () {
      if (duLieuDiaChi) {
        return Promise.resolve(duLieuDiaChi);
      }
      return fetch(form.action.replace(/\/dat-hang.*$/, "/data/dia-chi-2025.json"))
        .then(function (kq) { return kq.json(); })
        .then(function (d) { duLieuDiaChi = d; return d; });
    };

    var datPhuong = function (danhSach, giuLai) {
      chonPhuong.innerHTML = "";
      var dau = document.createElement("option");
      dau.value = "";
      dau.textContent = danhSach.length ? "Chọn phường / xã" : "Chọn tỉnh / thành phố trước";
      chonPhuong.appendChild(dau);
      danhSach.forEach(function (ten) {
        var o = document.createElement("option");
        o.value = ten;
        o.textContent = ten;
        if (ten === giuLai) {
          o.selected = true;
        }
        chonPhuong.appendChild(o);
      });
      chonPhuong.disabled = danhSach.length === 0;
    };

    chonTinh.addEventListener("change", function () {
      var tinh = chonTinh.value;
      if (!tinh) {
        datPhuong([], null);
        return;
      }
      chonPhuong.disabled = true;
      napDiaChi().then(function (d) {
        datPhuong(d[tinh] || [], null);
        chonPhuong.focus();
      }).catch(function () {
        chonPhuong.disabled = false;
      });
    });

    var capNhatTien = function () {
      var chon = form.querySelector('input[name="phuongThuc"]:checked');
      var pt = chon ? chon.value : "";
      var tong = pt === "CK" ? tamTinh - giamCk : tamTinh;
      document.querySelectorAll(".tt-tong-so").forEach(function (el) {
        el.textContent = dinhDang(tong);
      });
      document.querySelectorAll("[data-hien-khi]").forEach(function (el) {
        var dk = el.dataset.hienKhi;
        el.hidden = !(dk === pt || (dk === "da-chon" && pt));
      });
      if (hopUuDai) {
        hopUuDai.hidden = pt !== "CK";
      }
      form.querySelectorAll(".tt-pt__o").forEach(function (o) {
        var r = o.querySelector("input");
        o.classList.toggle("dang-chon", r.checked);
      });
    };

    form.addEventListener("change", function (e) {
      if (e.target.name === "phuongThuc") {
        capNhatTien();
      }
    });
    capNhatTien();

    var chonPt = function (giaTri) {
      var r = form.querySelector('input[name="phuongThuc"][value="' + giaTri + '"]');
      if (r && !r.disabled) {
        r.checked = true;
        capNhatTien();
      }
    };

    form.addEventListener("submit", function (e) {
      var chon = form.querySelector('input[name="phuongThuc"]:checked');
      if (chon && chon.value === "COD" && !daHoi && giamCk > 0 && hoi && typeof hoi.showModal === "function") {
        e.preventDefault();
        daHoi = true;
        hoi.showModal();
        return;
      }
      var nut = form.querySelector(".tt__dat");
      nut.disabled = true;
      nut.classList.add("dang-gui");
    });

    if (hoi) {
      hoi.querySelector("[data-chuyen-ck]").addEventListener("click", function () {
        hoi.close();
        chonPt("CK");
        form.requestSubmit ? form.requestSubmit() : form.submit();
      });
      hoi.querySelector("[data-tiep-cod]").addEventListener("click", function () {
        hoi.close();
        form.requestSubmit ? form.requestSubmit() : form.submit();
      });
    }

    var oMa = document.getElementById("maGiamGia");
    var nutApMa = document.getElementById("nut-ap-ma");
    var loiMa = document.getElementById("loi-ma");
    var maOk = document.getElementById("ma-ok");
    var dongMa = document.querySelector("[data-dong-ma]");

    var apMa = function () {
      var ma = oMa.value.trim().toUpperCase();
      oMa.value = ma;
      nutApMa.disabled = true;
      fetch(khung.dataset.kiemMa + "?ma=" + encodeURIComponent(ma), { headers: { "X-Requested-With": "XMLHttpRequest" } })
        .then(function (kq) { return kq.json(); })
        .then(function (d) {
          tamTinh = d.sauUuDai;
          giamCk = d.giamCk;
          loiMa.textContent = d.loi || "";
          oMa.setAttribute("aria-invalid", d.loi ? "true" : "false");
          maOk.textContent = d.ok ? "Đã áp dụng mã " + d.ma + ": " + d.moTaMa : "";
          if (!d.ok) {
            oMa.value = d.loi ? ma : "";
          }
          if (dongMa) {
            dongMa.hidden = !d.ok;
            dongMa.querySelector("[data-ten-ma]").textContent = d.ma || "";
            dongMa.querySelector("[data-tien-ma]").textContent = "−" + dinhDang(d.tienGiamMa);
          }
          document.querySelectorAll("[data-sau-uu-dai]").forEach(function (el) {
            el.textContent = dinhDang(d.sauUuDai);
          });
          document.querySelectorAll("[data-tien-ck]").forEach(function (el) {
            el.textContent = (el.tagName === "DD" ? "−" : "") + dinhDang(d.giamCk);
          });
          var cod = form.querySelector('input[name="phuongThuc"][value="COD"]');
          if (cod) {
            cod.disabled = !d.codDuocPhep;
            cod.closest(".tt-pt__o").classList.toggle("tt-pt__o--khoa", !d.codDuocPhep);
            if (!d.codDuocPhep && cod.checked) {
              cod.checked = false;
            }
          }
          capNhatTien();
        })
        .catch(function () {
          loiMa.textContent = "Không kiểm tra được mã, vui lòng thử lại";
        })
        .finally(function () {
          nutApMa.disabled = false;
        });
    };

    if (oMa && nutApMa) {
      nutApMa.addEventListener("click", function (e) {
        e.preventDefault();
        apMa();
      });
      oMa.addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
          e.preventDefault();
          apMa();
        }
      });
      document.querySelectorAll(".tt-phieu[data-ma]").forEach(function (phieu) {
        phieu.addEventListener("click", function () {
          oMa.value = phieu.dataset.ma;
          apMa();
        });
      });
    }

    var xoaLoi = function (e) {
      var o = e.target;
      if (o.id === "maGiamGia") {
        return;
      }
      var khoi = o.closest(".o-nhap, .tt-dong-y, .tt-nhom");
      if (o.name === "phuongThuc") {
        var nhom = o.closest(".tt-pt");
        nhom.classList.remove("tt-pt--loi");
        khoi = nhom.parentNode;
      }
      if (o.getAttribute("aria-invalid") === "true") {
        o.setAttribute("aria-invalid", "false");
      }
      if (khoi) {
        khoi.querySelectorAll(":scope > .loi-nhap").forEach(function (el) { el.remove(); });
      }
    };
    form.addEventListener("input", xoaLoi);
    form.addEventListener("change", xoaLoi);

    var loiDau = form.querySelector('[aria-invalid="true"]');
    if (loiDau) {
      loiDau.focus();
    }
  }

  document.querySelectorAll("[data-chep]").forEach(function (nut) {
    nut.addEventListener("click", function () {
      var xong = function () {
        nut.textContent = "Đã chép";
        setTimeout(function () { nut.textContent = "Chép"; }, 1600);
      };
      if (navigator.clipboard) {
        navigator.clipboard.writeText(nut.dataset.chep).then(xong);
      }
    });
  });

  var hopCk = document.querySelector(".dh-tt[data-kiem-tra]");
  if (hopCk) {
    var lan = 0;
    var kiemTra = function () {
      if (document.hidden || lan++ > 120) {
        return;
      }
      fetch(hopCk.dataset.kiemTra, { headers: { "X-Requested-With": "XMLHttpRequest" } })
        .then(function (kq) { return kq.ok ? kq.json() : null; })
        .then(function (d) {
          if (d && (d.daThanhToan || d.trangThai === "DA_HUY")) {
            location.reload();
          }
        })
        .catch(function () {});
    };
    setInterval(kiemTra, 5000);
  }
})();
