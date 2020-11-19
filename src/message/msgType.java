package message;

public enum msgType{
    REGISTER{
        public String toString() {
            return "REGISTER";
        }
    },
    REGISTERED{
        public String toString() {
            return "REGISTERED";
        }
    },
    REGISTER_DENIED{
        public String toString() {
            return "REGISTER-DENIED";
        }
    },
    DE_REGISTER{
        public String toString() {
            return "DE-REGISTER";
        }
    },
    UPDATE{
        public String toString() {
            return "UPDATE";
        }
    },
    UPDATE_CONFIRMED{
        public String toString() {
            return "UPDATE-CONFIRMED";
        }
    },
    UPDATE_DENIED{
        public String toString() {
            return "UPDATE-DENIED";
        }
    },
    SUBJECTS{
        public String toString() {
            return "SUBJECTS";
        }
    },
    SUBJECTS_UPDATED{
        public String toString() {
            return "SUBJECTS-UPDATED";
        }
    },
    SUBJECTS_REJECTED{
        public String toString() {
            return "SUBJECTS-REJECTED";
        }
    },
    PUBLISH{
        public String toString() {
            return "PUBLISH";
        }
    },
    MESSAGE{
        public String toString() {
            return "MESSAGE";
        }
    },
    PUBLISH_DENIED{
        public String toString() {
            return "PUBLISH-DENIED";
        }
    },
    CHANGE_SERVER{
        public String toString() {
            return "CHANGE-SERVER";
        }
    },
    UPDATE_SERVER{
        public String toString() {
            return "UPDATE-SERVER";
        }
    },
    SWITCH_SERVER{
        public String toString() {
            return "SWITCH-SERVER";
        }
    }
}