package com.ibrahim.soleeklabtask;

class Country {

    private String countryName;
    private String countryImageUrl;
    private String CountryCapital;
    private String countryRegion;

    public Country(String countryName, String countryImageUrl, String countryCapital, String countryRegion) {
        this.countryName = countryName;
        this.countryImageUrl = countryImageUrl;
        CountryCapital = countryCapital;
        this.countryRegion = countryRegion;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryImageUrl() {
        return countryImageUrl;
    }

    public String getCountryCapital() {
        return CountryCapital;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", countryImageUrl='" + countryImageUrl + '\'' +
                ", CountryCapital='" + CountryCapital + '\'' +
                ", countryRegion='" + countryRegion + '\'' +
                '}'+"\n";
    }
}
