package model;

import java.time.LocalDate;

/**
 *
 * @author jaret_000
 */
public class HoursWorked {
    private LocalDate dateWorked;
    private int hoursWorked;

    public HoursWorked(LocalDate dateWorked, int hoursWorked) {
        this.dateWorked = dateWorked;
        this.hoursWorked = hoursWorked;
    }

    public LocalDate getDateWorked() {
        return dateWorked;
    }

    public void setDateWorked(LocalDate dateWorked) {
        this.dateWorked = dateWorked;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
    
}
