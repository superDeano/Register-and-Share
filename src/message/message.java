package message;

enum msgType{
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
    }
}

public class message {

    String register, registered, register_denied, de_register;
    String update, update_confirmed, update_denied;
    String subjects, subjects_updated, subjects_rejected;
    String publish, message, publish_denied;
    String change_server, update_server;
    String RQ, name, IPaddress, socket, reason, list_of_subjects, subject, text;

    public message() {
        this.register = "";
        this.registered = "";
        this.register_denied = "";
        this.de_register = "";
        this.update = "";
        this.update_confirmed = "";
        this.update_denied = "";
        this.subjects = "";
        this.subjects_updated = "";
        this.subjects_rejected = "";
        this.publish = "";
        this.message = "";
        this.publish_denied = "";
        this.change_server = "";
        this.update_server = "";
        this.RQ = "";
        this.name = "";
        this.IPaddress = "";
        this.socket = "";
        this.reason = "";
        this.list_of_subjects = "";
        this.subject = "";
        this.text = "";
    }

    public String getRegister() {
        return register;
    }

    public String getRegister_denied() {
        return register_denied;
    }

    public String getDe_register() {
        return de_register;
    }

    public String getUpdate() {
        return update;
    }

    public String getUpdate_confirmed() {
        return update_confirmed;
    }

    public String getUpdate_denied() {
        return update_denied;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getSubjects_updated() {
        return subjects_updated;
    }

    public String getSubjects_rejected() {
        return subjects_rejected;
    }

    public String getPublish() {
        return publish;
    }

    public String getMessage() {
        return message;
    }

    public String getPublish_denied() {
        return publish_denied;
    }

    public String getChange_server() {
        return change_server;
    }

    public String getUpdate_server() {
        return update_server;
    }

    public String getRQ() {
        return RQ;
    }

    public String getName() {
        return name;
    }

    public String getIPaddress() {
        return IPaddress;
    }

    public String getSocket() {
        return socket;
    }

    public String getReason() {
        return reason;
    }

    public String getList_of_subjects() {
        return list_of_subjects;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public void setRegister_denied(String register_denied) {
        this.register_denied = register_denied;
    }

    public void setDe_register(String de_register) {
        this.de_register = de_register;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setUpdate_confirmed(String update_confirmed) {
        this.update_confirmed = update_confirmed;
    }

    public void setUpdate_denied(String update_denied) {
        this.update_denied = update_denied;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public void setSubjects_updated(String subjects_updated) {
        this.subjects_updated = subjects_updated;
    }

    public void setSubjects_rejected(String subjects_rejected) {
        this.subjects_rejected = subjects_rejected;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPublish_denied(String publish_denied) {
        this.publish_denied = publish_denied;
    }

    public void setChange_server(String change_server) {
        this.change_server = change_server;
    }

    public void setUpdate_server(String update_server) {
        this.update_server = update_server;
    }

    public void setRQ(String RQ) {
        this.RQ = RQ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIPaddress(String IPaddress) {
        this.IPaddress = IPaddress;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setList_of_subjects(String list_of_subjects) {
        this.list_of_subjects = list_of_subjects;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }
}
