package ca.pluszero.emotive.models;

public class Place {
    private final String description;
    private final String reference;

    public Place(String description, String reference) {
        this.description = description;
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (!description.equals(place.description)) return false;
        if (!reference.equals(place.reference)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + reference.hashCode();
        return result;
    }
}
