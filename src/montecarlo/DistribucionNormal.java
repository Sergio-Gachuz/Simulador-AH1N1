package montecarlo;
import cern.jet.random.Normal;

public class DistribucionNormal {
    
    double intervaloCerteza = 0.0;
    double limiteSuperior = 0.0;
    double alfaMedios = 0.0;

    public double getAlfaMedios() {
        return alfaMedios;
    }

    public void setAlfaMedios(double alfaMedios) {
        this.alfaMedios = alfaMedios;
    }

    public double getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(double limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public double getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(double limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public double getIntervaloCerteza() {
        return intervaloCerteza;
    }

    double limiteInferior = 0.0;
    
    public void setIntervaloCerteza(double intervaloNuevo) {
        Normal normalDistribution = new Normal(0.0, 1.0,
        Normal.makeDefaultGenerator());

        intervaloNuevo = (1.0 - (intervaloNuevo / 100.0)) / 2.0;
        alfaMedios = Ventana_Ri.getDecimal(4, intervaloNuevo);

        for (double i = 0.0; i >= -3.40; i -= 0.01) {

            double valor = normalDistribution.cdf(i);

            if (Ventana_Ri.getDecimal(4, valor) <= Ventana_Ri.getDecimal(4, intervaloNuevo)) {
                limiteInferior = Ventana_Ri.getDecimal(2, i);
                limiteSuperior = limiteInferior * -1;
                break;
            }

        }
    }


}
