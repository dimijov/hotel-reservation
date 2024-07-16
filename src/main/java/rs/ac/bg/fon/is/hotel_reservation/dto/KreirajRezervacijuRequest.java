package rs.ac.bg.fon.is.hotel_reservation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;
import rs.ac.bg.fon.is.hotel_reservation.model.Gost;

import java.time.LocalDate;
import java.util.List;

public class KreirajRezervacijuRequest {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    private LocalDate datumOd;

    @NotNull
    private LocalDate datumDo;

    @NotNull
    @Size(min = 1)
    private List<Gost> gosti;

    @NotNull
    private Long sobaId;

    private String promoKod;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LocalDate getDatumOd() {
            return datumOd;
        }

        public void setDatumOd(LocalDate datumOd) {
            this.datumOd = datumOd;
        }

        public LocalDate getDatumDo() {
            return datumDo;
        }

        public void setDatumDo(LocalDate datumDo) {
            this.datumDo = datumDo;
        }

        public List<Gost> getGosti() {
            return gosti;
        }

        public void setGosti(List<Gost> gosti) {
            this.gosti = gosti;
        }

        public Long getSobaId() {
            return sobaId;
        }

        public void setSobaId(Long sobaId) {
            this.sobaId = sobaId;
        }

        public String getPromoKod() {
            return promoKod;
        }

        public void setPromoKod(String promoKod) {
            this.promoKod = promoKod;
        }
}
