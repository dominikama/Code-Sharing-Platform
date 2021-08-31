package platform.business.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Class representing single code snippet, it's also JPA Entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "code_snippet")
@Getter
@Setter
public class Code {
    // == constants ==
    private static final String DATE_FORMATTER= "yyyy/MM/dd HH:mm:ss";

    // == fields ==

    @Column(name = "code")
    private String code;

    @Column(name = "date")
    private String date = formatDate(LocalDateTime.now());

    /**
     * Time restriction in seconds.
     */
    @Column(name = "time_left")
    private Long time;

    @Column(name = "views_amount")
    private Integer views = 0;

    @Column(name = "amount_of_time")
    @JsonIgnore
    private Long amountOfTime;

    @Column
    @JsonIgnore
    private Boolean triggered;

    @Column(name = "uuid")
    @JsonIgnore
    private String uuid;

    @Column(name = "id")
    @Id
    @JsonIgnore
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    // == public methods ==

    /**
     * Method responsible for calculating time and checking if time restriction hasn't been triggered.
     * @param dateNow current date
     * @return time in which snippet will be available.
     */
    public Long compareTime(LocalDateTime dateNow) {
        if (amountOfTime > 0) {
            this.time = amountOfTime - Duration.between(formatToLocalDate(date), dateNow).getSeconds();
            if (time < 0) {
                triggered = true;
            }
        }
        return time;
    }

    /**
     * Method responsible for decrementing left views amount and checking if views restriction hasn't been triggered.
     */
    public void decrementViews() {
        if (getViews() > 0) {
            if (getViews() - 1 == 0) {
                triggered = true;
            }
            views--;
        }
    }

    /**
     * Method which uses UUID class to generate random UUID for the snippet.
     */
    public void setUuid() {
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }

    /**
     * Method which formats current date.
     * @param date current date
     */
    public void setDate(LocalDateTime date) {
        this.date = formatDate(date);
    }

    // == private methods ==

    /**
     * Method to format a date.
     * @param date date which should be formatted
     * @return formatted date
     */
    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return formatter.format(date);
    }

    /**
     * Method to get LocalDateTime object from String parameter.
     * @param date String representation of the date
     * @return LocalDateTime object
     */
    public LocalDateTime formatToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return LocalDateTime.parse(date, formatter);
    }
}
