package com.example.demo.Servicios;



import com.example.demo.CustomerStatus;
import com.example.demo.Entidades.Customer;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomerService {

    @Autowired
    private static CustomerService instance;
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    private final HashMap<Long, Customer> contacts = new HashMap<>();
    private long nextId = 0;

    private CustomerService() {
    }

    /**
     * @return a reference to an example facade for Customer objects.
     */
    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
            instance.ensureTestData();
        }
        return instance;
    }

    /**
     * @return all available Customer objects.
     */
    public synchronized List<Customer> findAll() {
        return findAll(null);
    }

    /**
     * Finds all Customer's that match given filter.
     *
     * @param stringFilter
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @return list a Customer objects
     */
    public synchronized List<Customer> findAll(String stringFilter) {
        ArrayList<Customer> arrayList = new ArrayList<>();
        for (Customer contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Customer>() {

            @Override
            public int compare(Customer o1, Customer o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }

    /**
     * Finds all Customer's that match given filter and limits the resultset.
     *
     * @param stringFilter
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @param start
     *            the index of first result
     * @param maxresults
     *            maximum result count
     * @return list a Customer objects
     */
    public synchronized List<Customer> findAll(String stringFilter, int start, int maxresults) {
        ArrayList<Customer> arrayList = new ArrayList<>();
        for (Customer contact : contacts.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(contact.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Customer>() {

            @Override
            public int compare(Customer o1, Customer o2) {
                return (int) (o2.getId() - o1.getId());
            }
        });
        int end = start + maxresults;
        if (end > arrayList.size()) {
            end = arrayList.size();
        }
        return arrayList.subList(start, end);
    }

    /**
     * @return the amount of all customers in the system
     */
    public synchronized long count() {
        return contacts.size();
    }

    /**
     * Deletes a customer from a system
     *
     * @param value
     *            the Customer to be deleted
     */
    public synchronized void delete(Customer value) {
        contacts.remove(value.getId());
    }

    /**
     * Persists or updates customer in the system. Also assigns an identifier
     * for new Customer instances.
     *
     * @param entry
     */
    public synchronized void save(Customer entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "null.");
            return;
        }
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Customer) entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        contacts.put(entry.getId(), entry);
    }

    /**
     * Sample data generation
     */
    public void ensureTestData() {
        if (findAll().isEmpty()) {
            final String[] names = new String[] { "Jean Marte", "Stanley De Moya" , "Carlos Camacho" };
            Random r = new Random(0);
            for (String name : names) {
                String[] split = name.split(" ");
                Customer c = new Customer();
                c.setFirstName(split[0]);
                c.setLastName(split[1]);
                c.setEmail(split[0].toLowerCase() + "@" + split[1].toLowerCase() + ".com");
                c.setStatus(CustomerStatus.values()[r.nextInt(CustomerStatus.values().length)]);
                int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
                c.setBirthDate(LocalDate.now().plusDays(daysOld));
                save(c);
            }
        }
    }

 }
