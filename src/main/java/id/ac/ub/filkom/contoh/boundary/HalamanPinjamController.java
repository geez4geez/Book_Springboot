package id.ac.ub.filkom.contoh.boundary;

import id.ac.ub.filkom.contoh.control.Bukti;
import id.ac.ub.filkom.contoh.control.PinjamGagalException;
import id.ac.ub.filkom.contoh.control.PinjamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pinjam")
public class HalamanPinjamController {

    private static final String ANGGOTA_DEFAULT = "A001";

    private final PinjamService pinjamService;

    public HalamanPinjamController(PinjamService pinjamService) {
        this.pinjamService = pinjamService;
    }

    @GetMapping
    public String tampilHalaman(@RequestParam(required = false) String idAnggota,
                                HttpSession session, Model model) {
        if (idAnggota != null && !idAnggota.isBlank()) {
            session.setAttribute("idAnggota", idAnggota.trim());
        }
        return siapkanHalaman(session, model);
    }

    @PostMapping
    public String pilihBuku(@RequestParam String idBuku,
                            HttpSession session, Model model) {
        String idAnggota = idAnggotaLogin(session);
        try {
            Bukti bukti = pinjamService.pinjamBuku(idAnggota, idBuku);
            return tampilBukti(bukti, model);              
        } catch (PinjamGagalException e) {                  
            model.addAttribute("pesan", e.getMessage());
            model.addAttribute("bisaReservasi", e.isBisaReservasi());
            return siapkanHalaman(session, model);          
        }
    }

    private String siapkanHalaman(HttpSession session, Model model) {
        model.addAttribute("idAnggota", idAnggotaLogin(session));
        model.addAttribute("daftarBuku", pinjamService.daftarBuku());
        return "pinjam";                                    
    }

    /** +tampilBukti() pada class diagram 8a. */
    private String tampilBukti(Bukti bukti, Model model) {
        model.addAttribute("bukti", bukti);
        return "bukti";                                     
    }

    private String idAnggotaLogin(HttpSession session) {
        Object id = session.getAttribute("idAnggota");
        return id != null ? id.toString() : ANGGOTA_DEFAULT;
    }
}