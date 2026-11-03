package id.ac.ub.filkom.contoh.control;

public class PinjamGagalException extends RuntimeException {

    private final boolean bisaReservasi;

    private PinjamGagalException(String pesan, boolean bisaReservasi) {
        super(pesan);
        this.bisaReservasi = bisaReservasi;
    }

    public static PinjamGagalException tidakDitemukan(String objek) {
        return new PinjamGagalException(objek + " tidak ditemukan", false);
    }

    public static PinjamGagalException melewatiBatas() {
        return new PinjamGagalException("Melewati batas pinjam", false);
    }

    public static PinjamGagalException stokHabis() {
        return new PinjamGagalException("Stok buku habis", true);
    }

    public boolean isBisaReservasi() { return bisaReservasi; }
}