(function () {
  'use strict';

  var itChuyenDong = window.matchMedia &&
                     window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  var quanSatThem = null;

  var batDauHienDan = null;

  function moMan() {
    var goc = document.documentElement;
    var man = document.getElementById('mo-man');

    if (!man) {
      goc.classList.remove('dang-mo-man');
      goc.classList.remove('khoa-cuon');
      return;
    }

    if (!goc.classList.contains('dang-mo-man')) {
      if (man.parentNode) man.parentNode.removeChild(man);
      return;
    }

    var so = function (ten, macDinh) {
      var v = parseInt(man.getAttribute(ten), 10);
      return isNaN(v) ? macDinh : v;
    };
    var VE = so('data-ve', 1700);
    var REM = 1300;

    var xongRoi = false;
    function dong() {
      if (xongRoi) return;
      xongRoi = true;
      man.classList.add('xong');

      setTimeout(function () { goc.classList.remove('khoa-cuon'); }, 200);

      setTimeout(function () {
        goc.classList.remove('dang-mo-man');
        if (man.parentNode) man.parentNode.removeChild(man);
      }, REM);
    }

    setTimeout(dong, VE);

    man.addEventListener('click', dong);
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' || e.key === 'Enter' || e.key === ' ') dong();
    });

    setTimeout(dong, VE + 4000);
  }

  function bannerDoiAnh() {
    var banner = document.getElementById('banner');
    if (!banner) return;

    var anh = banner.querySelectorAll('.banner__anh');
    var cham = banner.querySelectorAll('.banner__cham button');
    if (anh.length < 2) return;

    var i = 0, hen = null, GIAY = 5500;

    function chieu(k) {
      i = (k + anh.length) % anh.length;
      for (var n = 0; n < anh.length; n++) {
        if (n === i) { anh[n].classList.add('hien'); } else { anh[n].classList.remove('hien'); }
      }
      for (var m = 0; m < cham.length; m++) {
        cham[m].setAttribute('aria-current', m === i ? 'true' : 'false');
      }
    }

    function henLai() {
      clearTimeout(hen);
      if (!itChuyenDong) hen = setTimeout(function () { chieu(i + 1); henLai(); }, GIAY);
    }

    for (var m = 0; m < cham.length; m++) {
      (function (n) {
        cham[n].addEventListener('click', function () { chieu(n); henLai(); });
      })(m);
    }

    banner.addEventListener('mouseenter', function () { clearTimeout(hen); });
    banner.addEventListener('mouseleave', henLai);

    document.addEventListener('visibilitychange', function () {
      if (document.hidden) { clearTimeout(hen); } else { henLai(); }
    });

    chieu(0);
    henLai();
  }

  function thanhDieuHuong() {
    var thanh = document.getElementById('thanh');
    if (!thanh) return;

    var banner = document.getElementById('banner');
    if (!banner) { thanh.classList.add('dinh'); return; }

    function tinh() {
      thanh.classList.toggle('dinh', window.scrollY > banner.offsetHeight - 90);
    }
    window.addEventListener('scroll', tinh, { passive: true });
    window.addEventListener('resize', tinh);
    tinh();
  }

  function menuManHep() {
    var khay = document.getElementById('menu-nho');
    var moNut = document.getElementById('mo-menu');
    var dongNut = document.getElementById('dong-menu');
    if (!khay || !moNut) return;

    function bat(mo) {
      khay.classList.toggle('mo', mo);
      document.body.style.overflow = mo ? 'hidden' : '';
      moNut.setAttribute('aria-expanded', mo ? 'true' : 'false');
    }

    moNut.addEventListener('click', function () { bat(true); });
    if (dongNut) dongNut.addEventListener('click', function () { bat(false); });

    khay.addEventListener('click', function (e) { if (e.target === khay) bat(false); });

    var lienKet = khay.querySelectorAll('a');
    for (var i = 0; i < lienKet.length; i++) {
      lienKet[i].addEventListener('click', function () { bat(false); });
    }

    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && khay.classList.contains('mo')) bat(false);
    });
  }

  function oTimKiem() {
    var thanh = document.getElementById('thanh');
    var nut = document.getElementById('bat-tim');
    var nhap = document.getElementById('o-tim-nhap');
    if (!thanh || !nut || !nhap) return;

    function bat(mo) {
      thanh.classList.toggle('tim', mo);
      nut.setAttribute('aria-expanded', mo ? 'true' : 'false');
      if (mo) { nhap.focus(); } else { nhap.blur(); }
    }

    nut.addEventListener('click', function (e) {
      e.stopPropagation();
      bat(!thanh.classList.contains('tim'));
    });

    document.addEventListener('click', function (e) {
      if (!thanh.classList.contains('tim')) return;
      if (e.target === nhap || nhap.contains(e.target)) return;
      if (nhap.value === '') bat(false);
    });

    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && thanh.classList.contains('tim')) bat(false);
    });
  }

  function doiAnhDongSanPham() {
    var dai = document.getElementById('dong-sp');
    if (!dai) return;

    var anh = dai.querySelectorAll('.dong-sp__anh');
    var o = dai.querySelectorAll('.o-sp');
    if (anh.length === 0 || o.length === 0) return;

    function chieu(k) {
      for (var n = 0; n < anh.length; n++) {
        if (n === k) { anh[n].classList.add('hien'); } else { anh[n].classList.remove('hien'); }
      }
    }

    for (var i = 0; i < o.length; i++) {
      (function (el) {
        var k = parseInt(el.getAttribute('data-anh'), 10);
        if (isNaN(k)) return;
        el.addEventListener('mouseenter', function () { chieu(k); });
        el.addEventListener('focus', function () { chieu(k); });
      })(o[i]);
    }
  }

  function locSong() {
    var form = document.querySelector('form.ds');
    if (!form) return;

    if (!window.fetch || !window.history || !window.history.pushState) return;

    var vung = document.getElementById('kq');
    var dem = document.getElementById('dem-so');
    var diaChiLay = form.getAttribute('data-lay');
    if (!vung || !diaChiLay) return;

    function thamSo() {
      var d = new FormData(form);
      var q = [];
      d.forEach(function (v, k) {
        if (v === '') return;
        q.push(encodeURIComponent(k) + '=' + encodeURIComponent(v));
      });
      return q.join('&');
    }

    function tai(qs, nutDay) {
      vung.classList.add('dang-tai');

      fetch(diaChiLay + '?' + qs, {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
      })
        .then(function (r) {
          if (!r.ok) throw new Error(r.status);
          return r.text();
        })
        .then(function (html) {

          var tam = document.createElement('div');
          tam.innerHTML = html;
          var o = tam.firstElementChild;
          if (!o) throw new Error('rong');

          vung.parentNode.replaceChild(o, vung);
          vung = o;

          if (dem) dem.textContent = vung.getAttribute('data-so') || '0';

          if (quanSatThem) quanSatThem(vung);

          if (nutDay) {
            window.history.pushState(null, '', form.action.split('?')[0] + '?' + qs);
          }
        })
        .catch(function () {

          form.submit();
        });
    }

    form.addEventListener('change', function (e) {
      var o = e.target;
      if (o.type !== 'checkbox' && o.tagName !== 'SELECT') return;
      tai(thamSo(), true);
    });

    form.addEventListener('click', function (e) {
      var a = e.target.closest ? e.target.closest('.trang__nut') : null;
      if (!a) return;

      e.preventDefault();
      tai(a.getAttribute('href').split('?')[1] || '', true);

      var thanh = document.querySelector('.ds__thanh');
      if (thanh) thanh.scrollIntoView({ behavior: 'smooth', block: 'start' });
    });

    window.addEventListener('popstate', function () {
      var qs = window.location.search.replace(/^\?/, '');

      var p = new URLSearchParams(qs);
      var o = form.querySelectorAll('input[type="checkbox"]');
      for (var i = 0; i < o.length; i++) {
        o[i].checked = p.getAll(o[i].name).indexOf(o[i].value) >= 0;
      }
      var sx = form.querySelector('#sapXep');
      if (sx) sx.value = p.get('sapXep') || sx.options[0].value;

      tai(qs, false);
    });
  }

  function thuGonBoLoc() {
    if (window.innerWidth >= 900) return;
    var ds = document.querySelectorAll('.loc__nhom');
    for (var i = 0; i < ds.length; i++) {

      if (ds[i].querySelector('input:checked')) continue;
      ds[i].removeAttribute('open');
    }
  }

  function luoiChuyen() {
    var g = document.documentElement;
    if (g.className.indexOf('dang-luoi') < 0) return;

    var hop = document.getElementById('luoi-chuyen');
    if (!hop) { go(); return; }

    var cot  = Math.min(28, Math.max(5, Math.round(window.innerWidth  / 60)));
    var hang = Math.min(15, Math.max(4, Math.round(window.innerHeight / 60)));

    hop.style.gridTemplateColumns = 'repeat(' + cot + ', 1fr)';
    hop.style.gridTemplateRows    = 'repeat(' + hang + ', 1fr)';

    var gx = (cot - 1) / 2;
    var gy = (hang - 1) / 2;

    var BUOC = 27;
    var DAI  = 560;

    var chuoi = '';
    var xa = 0;
    for (var y = 0; y < hang; y++) {
      for (var x = 0; x < cot; x++) {
        var d = Math.sqrt((x - gx) * (x - gx) + (y - gy) * (y - gy));
        if (d > xa) xa = d;
        chuoi += '<span style="animation-delay:' + Math.round(d * BUOC) + 'ms"></span>';
      }
    }
    hop.innerHTML = chuoi;
    hop.className += ' co-o';

    var tong = Math.round(xa * BUOC) + DAI;

    setTimeout(go, tong + 60);

    setTimeout(function () {
      if (batDauHienDan) batDauHienDan();
    }, Math.round(tong * 0.6));

    function go() {
      g.className = g.className.replace(/ *\bdang-luoi\b/g, '');
    }
  }

  function boAnhChiTiet() {
    var to = document.getElementById('anh-to');
    var nho = document.querySelectorAll('.gal__nho');
    if (!to || !nho.length) return;

    for (var i = 0; i < nho.length; i++) {
      nho[i].addEventListener('click', function () {
        var d = this.getAttribute('data-anh');
        if (!d) return;

        to.src = d;
        for (var k = 0; k < nho.length; k++) nho[k].classList.remove('dang-chon');
        this.classList.add('dang-chon');
      });
    }
  }

  function oDemSoLuong() {
    var o = document.getElementById('so-luong');
    if (!o) return;

    var nut = document.querySelectorAll('[data-dem]');
    for (var i = 0; i < nut.length; i++) {
      nut[i].addEventListener('click', function () {
        var moi = (parseInt(o.value, 10) || 1) + parseInt(this.getAttribute('data-dem'), 10);

        var it = parseInt(o.min, 10) || 1;
        var nhieu = parseInt(o.max, 10) || 99;
        o.value = Math.max(it, Math.min(nhieu, moi));
      });
    }
  }

  function theNoiDung() {
    var nav = document.querySelector('.tab__nav');
    if (!nav) return;

    var nut = nav.querySelectorAll('.tab__nut');

    function chon(i) {
      for (var k = 0; k < nut.length; k++) {
        var bat = (k === i);
        nut[k].classList.toggle('dang-chon', bat);
        nut[k].setAttribute('aria-selected', bat ? 'true' : 'false');

        var o = document.getElementById(nut[k].getAttribute('aria-controls'));
        if (o) o.classList.toggle('hien', bat);
      }
    }

    for (var i = 0; i < nut.length; i++) {
      (function (i) {
        nut[i].addEventListener('click', function () { chon(i); });

        nut[i].addEventListener('keydown', function (e) {
          var b = null;
          if (e.key === 'ArrowRight') b = (i + 1) % nut.length;
          if (e.key === 'ArrowLeft') b = (i - 1 + nut.length) % nut.length;
          if (b === null) return;
          e.preventDefault();
          chon(b);
          nut[b].focus();
        });
      })(i);
    }
  }

  function hienDan() {
    var g = document.documentElement;

    var khoi = document.querySelectorAll('.hien-dan, .hien-dan-nhom');
    if (!khoi.length) {

      g.className += ' hien-san-sang';
      return;
    }

    function hienNgay(ds) {
      for (var i = 0; i < ds.length; i++) danhDau(ds[i]);
    }

    function danhDau(o) {

      if (o.className.indexOf('hien-dan-nhom') >= 0) {
        var con = o.children;
        for (var k = 0; k < con.length; k++) {
          con[k].style.setProperty('--i', k);
        }
      }
      o.classList.add('da-hien');
    }

    if (!('IntersectionObserver' in window)) {
      g.className += ' hien-san-sang';
      hienNgay(khoi);
      return;
    }

    var mat = new IntersectionObserver(function (dsGap) {
      for (var i = 0; i < dsGap.length; i++) {
        if (!dsGap[i].isIntersecting) continue;
        danhDau(dsGap[i].target);

        mat.unobserve(dsGap[i].target);
      }
    }, {

      rootMargin: '0px 0px -12% 0px',
      threshold: 0
    });

    function trongTamNhin(o) {
      var r = o.getBoundingClientRect();
      return r.top < (window.innerHeight || 0) && r.bottom > 0;
    }

    function theoDoi(goc) {
      var ds = (goc || document).querySelectorAll('.hien-dan, .hien-dan-nhom');

      for (var i = 0; i < ds.length; i++) {
        if (ds[i].className.indexOf('da-hien') >= 0) continue;

        if (trongTamNhin(ds[i])) {
          danhDau(ds[i]);
          continue;
        }

        mat.observe(ds[i]);
      }
    }

    quanSatThem = theoDoi;

    function batDau() {

      if (g.className.indexOf('hien-san-sang') >= 0) return;

      g.className += ' hien-san-sang';
      theoDoi(document);
    }

    if (g.className.indexOf('dang-mo-man') >= 0) {
      khiManTat(batDau);
      return;
    }

    if (g.className.indexOf('dang-luoi') >= 0) {
      batDauHienDan = batDau;

      setTimeout(function () {
        if (g.className.indexOf('hien-san-sang') < 0) batDau();
      }, 1200);
      return;
    }

    batDau();
  }

  function khiManTat(xong) {
    var g = document.documentElement;

    function conChe() {
      return g.className.indexOf('dang-mo-man') >= 0;
    }

    if (!conChe()) { xong(); return; }

    if (!('MutationObserver' in window)) { setTimeout(xong, 2500); return; }

    var mat = new MutationObserver(function () {
      if (conChe()) return;
      mat.disconnect();
      xong();
    });
    mat.observe(g, { attributes: true, attributeFilter: ['class'] });
  }

  function nutInHoaDon() {
    var n = document.getElementById('in-hoa-don');
    if (!n) return;

    n.hidden = false;
    n.addEventListener('click', function () { window.print(); });
  }

  function chay() {
    moMan();
    bannerDoiAnh();
    thanhDieuHuong();
    menuManHep();
    oTimKiem();
    doiAnhDongSanPham();
    locSong();
    thuGonBoLoc();
    boAnhChiTiet();
    oDemSoLuong();
    theNoiDung();
    nutInHoaDon();
    hienDan();
    luoiChuyen();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', chay);
  } else {
    chay();
  }
})();
