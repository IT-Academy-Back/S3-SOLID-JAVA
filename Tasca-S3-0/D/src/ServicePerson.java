public class ServicePerson {
    public void savePerson(Person person) {
        MySql mysql = new MySql();
        mysql.savePerson(person);
    }

}