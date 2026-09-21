/* ============================================================
   Bếp Nè : hiệu ứng giao diện
   JavaScript thuần, không jQuery, không thư viện ngoài.
   ============================================================ */
(function () {
  'use strict';

  var $  = function (s, g) { return (g || document).querySelector(s); };
  var $$ = function (s, g) { return Array.prototype.slice.call((g || document).querySelectorAll(s)); };
  var itChuyenDong = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  /* ---------- 1. Hiện dần khi cuộn tới ---------- */
  function hienDan() {
    var moc = $$('.hien-dan');
    if (itChuyenDong || !('IntersectionObserver' in window)) {
      moc.forEach(function (el) { el.classList.add('da-hien'); });
      return;
    }
    var td = new IntersectionObserver(function (ms) {
      ms.forEach(function (m) {
        if (m.isIntersecting) { m.target.classList.add('da-hien'); td.unobserve(m.target); }
      });
    }, { threshold: 0.1, rootMargin: '0px 0px -6% 0px' });
    moc.forEach(function (el) { td.observe(el); });
  }

  /* ---------- 2. Thanh dính: chỉ trượt xuống sau khi cuộn qua banner ----------
     Cuộn tới đâu thì thanh nổi ở đáy banner tắt đi, tránh hai menu cùng hiện. */
  function thanhMenu() {
    var dinh = $('#thanh-dinh');
    if (!dinh) return;
    var noi = $('#thanh-noi');
    var banner = $('#banner');

    // Trang nào không có banner thì để thanh dính hiện luôn
    if (!banner || !noi) { dinh.classList.add('hien'); return; }

    function tinh() {
      var moc = banner.offsetTop + banner.offsetHeight - dinh.offsetHeight - 40;
      var qua = window.scrollY > moc;
      dinh.classList.toggle('hien', qua);
      noi.classList.toggle('tat', qua);
    }
    window.addEventListener('scroll', tinh, { passive: true });
    window.addEventListener('resize', tinh);
    tinh();
  }

  /* ---------- 3. Banner: slide tự chạy, có nút trước / tạm dừng / kế tiếp ---------- */
  function bannerSlide() {
    var banner = $('#banner');
    if (!banner) return;
    var slide = $$('.banner__slide', banner);
    var cham  = $$('.banner__cham button', banner);
    if (slide.length < 2) return;

    var i = 0, hen = null, dung = itChuyenDong, GIAY = 7000;
    var nutTam = $('.banner__tam', banner);

    function chieu(k) {
      i = (k + slide.length) % slide.length;
      slide.forEach(function (s, n) { s.classList.toggle('dang-chieu', n === i); });
      cham.forEach(function (c, n) {
        c.setAttribute('aria-current', n === i ? 'true' : 'false');
        // Ép vẽ lại để thanh tiến trình chạy lại từ đầu
        if (n === i) { c.style.animation = 'none'; void c.offsetWidth; c.style.animation = ''; }
      });
      dat();
    }
    function dat() {
      clearTimeout(hen);
      if (!dung) hen = setTimeout(function () { chieu(i + 1); }, GIAY);
    }

    cham.forEach(function (c, n) { c.addEventListener('click', function () { chieu(n); }); });

    var truoc = $('.banner__truoc', banner), sau = $('.banner__sau', banner);
    if (truoc) truoc.addEventListener('click', function () { chieu(i - 1); });
    if (sau)   sau.addEventListener('click', function () { chieu(i + 1); });

    if (nutTam) {
      nutTam.addEventListener('click', function () {
        dung = !dung;
        nutTam.setAttribute('aria-pressed', dung ? 'true' : 'false');
        nutTam.setAttribute('aria-label', dung ? 'Chạy tiếp' : 'Tạm dừng tự chạy');
        nutTam.innerHTML = '<svg class="ic"><use xlink:href="#ic-' +
                           (dung ? 'phat' : 'tam-dung') + '" /></svg>';
        // Đang dừng thì thanh tiến trình cũng phải đứng yên
        cham.forEach(function (c) { c.style.animationPlayState = dung ? 'paused' : 'running'; });
        dat();
      });
    }

    document.addEventListener('visibilitychange', function () {
      if (document.hidden) clearTimeout(hen); else dat();
    });

    chieu(0);
  }

  /* ---------- 4. Băng chuyền khuyến mãi, chạy vòng lặp vô tận ----------
     Nhân bản một lượt slide ở trước và một lượt ở sau, thành ba khối liền nhau.
     Luôn chạy quanh khối giữa; hễ trượt lố sang khối nhân bản thì ngay sau khi
     hiệu ứng chạy xong sẽ nhảy thầm về đúng slide tương ứng ở khối giữa,
     nên người xem thấy dải slide chạy liền mạch không có điểm cuối. */
  function bangKhuyenMai() {
    var bang = $('#km-bang');
    if (!bang) return;
    var day = $('#km-day', bang);
    if (!day) return;

    var goc = $$('.km-slide', day);
    var N = goc.length;
    if (N === 0) return;

    var cham  = $$('#km-cham button');
    var truoc = $('.km-nut--truoc', bang);
    var sau   = $('.km-nut--sau', bang);

    // Nhân bản hai đầu. Bản sao chỉ để nhìn nên giấu khỏi trình đọc màn hình
    // và khỏi thứ tự nhấn Tab.
    if (N > 1) {
      var truocKhoi = document.createDocumentFragment();
      var sauKhoi   = document.createDocumentFragment();
      goc.forEach(function (s) {
        [truocKhoi, sauKhoi].forEach(function (khoi) {
          var c = s.cloneNode(true);
          c.setAttribute('aria-hidden', 'true');
          $$('a', c).forEach(function (x) { x.setAttribute('tabindex', '-1'); });
          khoi.appendChild(c);
        });
      });
      day.insertBefore(truocKhoi, goc[0]);
      day.appendChild(sauKhoi);
    }

    var slide = $$('.km-slide', day);
    var i = (N > 1) ? N : 0;          // bắt đầu ở slide đầu của khối giữa
    var hen = null, TU_CHAY = 5500;

    function dat(muot) {
      var s = slide[i];
      var lech = bang.clientWidth / 2 - (s.offsetLeft + s.offsetWidth / 2);
      if (muot === false) day.style.transition = 'none';
      day.style.transform = 'translateX(' + Math.round(lech) + 'px)';
      if (muot === false) { void day.offsetWidth; day.style.transition = ''; }

      slide.forEach(function (e, n) { e.classList.toggle('giua', n === i); });
      var thu = (N > 1) ? ((i - N) % N + N) % N : 0;
      cham.forEach(function (c, n) { c.setAttribute('aria-current', n === thu ? 'true' : 'false'); });
    }

    // Kéo chỉ số về khối giữa mà không chạy hiệu ứng, người xem không nhận ra
    function veKhoiGiua() {
      if (N < 2) return;
      var moi = i;
      if (i < N) moi = i + N;
      else if (i >= 2 * N) moi = i - N;
      if (moi !== i) { i = moi; dat(false); }
    }

    function di(k) { i = k; dat(true); henLai(); }

    function henLai() {
      clearTimeout(hen);
      if (N > 1 && !itChuyenDong) hen = setTimeout(function () { di(i + 1); }, TU_CHAY);
    }

    day.addEventListener('transitionend', function (e) {
      if (e.target === day && e.propertyName === 'transform') veKhoiGiua();
    });

    if (truoc) truoc.addEventListener('click', function () { di(i - 1); });
    if (sau)   sau.addEventListener('click', function () { di(i + 1); });

    // Bấm chấm: chọn bản sao gần vị trí hiện tại nhất để đường trượt ngắn nhất
    cham.forEach(function (c, n) {
      c.addEventListener('click', function () {
        if (N < 2) return;
        var chon = n, ganNhat = Infinity;
        [n, n + N, n + 2 * N].forEach(function (u) {
          var d = Math.abs(u - i);
          if (d < ganNhat) { ganNhat = d; chon = u; }
        });
        di(chon);
      });
    });

    // Bấm vào slide bên cạnh thì đưa nó ra giữa, không mở liên kết
    slide.forEach(function (s, n) {
      s.addEventListener('click', function (e) {
        if (n !== i) { e.preventDefault(); di(n); }
      });
    });

    // Vuốt trên màn hình cảm ứng
    var x0 = null;
    day.addEventListener('touchstart', function (e) { x0 = e.touches[0].clientX; }, { passive: true });
    day.addEventListener('touchend', function (e) {
      if (x0 === null) return;
      var dx = e.changedTouches[0].clientX - x0;
      if (Math.abs(dx) > 40) di(i + (dx < 0 ? 1 : -1));
      x0 = null;
    });

    // Rê chuột vào thì dừng tự chạy
    bang.addEventListener('mouseenter', function () { clearTimeout(hen); });
    bang.addEventListener('mouseleave', henLai);
    bang.addEventListener('focusin',  function () { clearTimeout(hen); });
    bang.addEventListener('focusout', henLai);
    document.addEventListener('visibilitychange', function () {
      if (document.hidden) clearTimeout(hen); else henLai();
    });

    window.addEventListener('resize', function () { dat(false); });
    dat(false);
    henLai();
  }

  /* ---------- 5. Menu cho màn hình nhỏ ---------- */
  function menuNho() {
    var m = $('#menu-mb');
    if (!m) return;
    var moNut = $$('[data-mo-menu]');
    var dong = $('#dong-menu');

    function bat(tt) {
      m.classList.toggle('mo', tt);
      document.body.style.overflow = tt ? 'hidden' : '';
      moNut.forEach(function (n) { n.setAttribute('aria-expanded', tt ? 'true' : 'false'); });
    }
    moNut.forEach(function (n) { n.addEventListener('click', function () { bat(true); }); });
    if (dong) dong.addEventListener('click', function () { bat(false); });
    m.addEventListener('click', function (e) { if (e.target === m) bat(false); });
    $$('a', m).forEach(function (a) { a.addEventListener('click', function () { bat(false); }); });
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && m.classList.contains('mo')) bat(false);
    });
  }

  /* ---------- 6. Nút lên đầu trang ---------- */
  function lenDau() {
    var n = $('#len-dau');
    if (!n) return;
    window.addEventListener('scroll', function () {
      n.classList.toggle('hien', window.scrollY > 700);
    }, { passive: true });
    n.addEventListener('click', function () {
      window.scrollTo({ top: 0, behavior: itChuyenDong ? 'auto' : 'smooth' });
    });
  }

  /* ---------- 7. Cuộn ngang dải tin tức ---------- */
  function cuonTin() {
    var day = $('#cuon-tin');
    if (!day) return;
    $$('[data-cuon]').forEach(function (n) {
      n.addEventListener('click', function () {
        var buoc = day.firstElementChild ? day.firstElementChild.offsetWidth + 18 : 300;
        day.scrollBy({ left: buoc * parseInt(n.getAttribute('data-cuon'), 10),
                       behavior: itChuyenDong ? 'auto' : 'smooth' });
      });
    });
  }

  function chay() {
    hienDan();
    thanhMenu();
    bannerSlide();
    bangKhuyenMai();
    menuNho();
    lenDau();
    cuonTin();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', chay);
  } else {
    chay();
  }
})();
