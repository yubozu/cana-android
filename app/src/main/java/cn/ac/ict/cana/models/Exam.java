package cn.ac.ict.cana.models;

/**
 * Author: saukymo
 * Date: 9/13/16
 */
public class Exam {
    public final String name;

    @Deprecated
    public Exam(String examName) {
        name = examName;
    }

    public Exam(Builder builder) {
        this.name = builder.name;
    }

    public static class Builder {

        private String name;

        public Builder() {
        }

        public Builder setActivity() {
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Exam build() {
            return new Exam(this);
        }
    }
}
