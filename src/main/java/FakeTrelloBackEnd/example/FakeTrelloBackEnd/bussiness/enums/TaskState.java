package FakeTrelloBackEnd.example.FakeTrelloBackEnd.bussiness.enums;

public enum TaskState {
    TODO{
        @Override
        public String toString() {
            return "toDo";
        }
    },
    INPROGRESS{
        @Override
        public String toString() {
            return "inprogress";
        }
    },
    DONE{
        @Override
        public String toString() {
            return "done";
        }
    }
}
