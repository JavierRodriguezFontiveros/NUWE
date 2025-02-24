package com.hackathon.blockchain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne(fetch = FetchType.LAZY)  // Evita cargas innecesarias
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    // Constructor sin ID porque se genera automáticamente
    public Asset(String symbol, double quantity, Wallet wallet) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.wallet = wallet;
    }

    // Método getValue() para obtener el valor de la cantidad de activos
    public double getValue() {
        // Aquí puedes definir la lógica para obtener el valor de un activo.
        // Por ejemplo, si el valor de cada símbolo está en algún servicio de mercado,
        // puedes calcular el valor total de los activos.
        // En este caso, se hace un valor ficticio, asumiendo que "quantity" es el valor.

        // Si tienes un servicio de mercado que devuelve el valor del activo por símbolo,
        // aquí podrías invocar esa lógica, algo como:
        // double assetValue = marketDataService.getPriceForSymbol(this.symbol);

        // Por ahora, devolvemos solo quantity como valor. Puedes reemplazarlo con la lógica adecuada.
        return quantity; // Aquí deberías reemplazar con la lógica real para calcular el valor
    }
}
