package com.aston.infoBoardRestService.util;


import com.aston.infoBoardRestService.entity.User;

import java.util.List;
import java.util.Random;


public class UserGenerator {
    private static final List<String> NAMES = List.of(
            "Alice", "Charlie", "John", "Emma", "Michael", "Sarah", "David", "Emily", "Ryan", "Olivia",
            "Daniel", "Sophia", "James", "Ava", "Matthew", "Isabella", "Christopher", "Emma", "Alexander", "Madison",
            "Liam", "Noah", "William", "Elijah", "Benjamin", "Lucas", "Mason", "Ethan", "Logan", "Jacob",
            "Mia", "Amelia", "Harper", "Evelyn", "Abigail", "Ella", "Elizabeth", "Camila", "Luna", "Sofia",
            "Jackson", "Sebastian", "Aiden", "Owen", "Samuel", "Henry", "Joseph", "Gabriel", "Carter", "Jayden"
    );

    private static final List<String> SURNAMES = List.of(
            "Johnson", "Brown", "Doe", "Smith", "Johnson", "Lee", "Brown", "Wilson", "Miller", "Taylor",
            "Martinez", "Anderson", "Clark", "Harris", "Young", "Lopez", "King", "Johnson", "Davis", "Moore",
            "Garcia", "Martinez", "Robinson", "Walker", "Perez", "Hall", "Young", "Allen", "Sanchez", "Wright",
            "King", "Scott", "Green", "Baker", "Adams", "Nelson", "Hill", "Ramirez", "Campbell", "Mitchell",
            "Carter", "Roberts", "Phillips", "Evans", "Turner", "Torres", "Parker", "Collins", "Edwards", "Stewart"
    );
    private static final Random RANDOM = new Random();


    public static User generateUser() {
        String name = generateRandomName();
        String surname = generateRandomSurname();
        return new User(String.format("%s %s", name, surname), EmailGenerator.generateRandomEmail(name, surname));
    }


    private static String generateRandomName() {
        return NAMES.get(RANDOM.nextInt(NAMES.size()));
    }

    private static String generateRandomSurname() {
        return SURNAMES.get(RANDOM.nextInt(SURNAMES.size()));
    }

    private static class EmailGenerator {
        private static final List<String> DOMAINS = List.of(
                "example.com", "test.com", "sample.org", "demo.net", "mail.com",
                "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "aol.com",
                "live.com", "icloud.com", "protonmail.com", "zoho.com", "yandex.com",
                "example.net", "example.org", "test.net", "test.org", "sample.net",
                "demo.org", "mail.net", "mail.org", "service.com", "service.net",
                "service.org", "webmail.com", "webmail.net", "webmail.org"
        );

        public static String generateRandomEmail(String name, String surname) {
            String localPart = generateLocalPartFromName(name, surname);
            String domain = generateRandomDomain();
            return localPart + "@" + domain;
        }

        private static String generateLocalPartFromName(String name, String surname) {
            // Use the name and surname to create a local part
            String localPart = name.toLowerCase() + "." + surname.toLowerCase();
            // Remove any spaces or special characters
            localPart = localPart.replaceAll("[^a-zA-Z0-9.]", "");
            // Add 3 random digits with leading zeros to ensure uniqueness
            int randomDigits = RANDOM.nextInt(1000); // 0-999
            String formattedDigits = String.format("%03d", randomDigits); // Ensure leading zeros
            localPart += formattedDigits;
            return localPart;
        }

        private static String generateRandomDomain() {
            return DOMAINS.get(RANDOM.nextInt(DOMAINS.size()));
        }
    }


}
