package com.example.demo;

import com.example.demo.Entidades.Customer;
import com.example.demo.Servicios.CustomerService;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout {

    private TextField nombre = new TextField("Nombre");
    private TextField apellido = new TextField("Apellido");
    private TextField email = new TextField("Email");
    private NativeSelect<CustomerStatus> status = new NativeSelect<>("Status");
    private DateField fecha = new DateField("Fecha");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private CustomerService service = CustomerService.getInstance();
    private Customer customer;
    private MyUI myUI;
    private Binder<Customer> binder = new Binder<>(Customer.class);

    public CustomerForm(MyUI myUI) {
        this.myUI = myUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        addComponents(nombre, apellido, email, status, fecha, buttons);

        status.setItems(CustomerStatus.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        binder.setBean(customer);

        // Show delete button for only customers already in the database
        delete.setVisible(customer.isPersisted());
        setVisible(true);
        nombre.selectAll();
    }

    private void delete() {
        service.delete(customer);
        myUI.updateList();
        setVisible(false);
    }

    private void save() {
        service.save(customer);
        myUI.updateList();
        setVisible(false);
    }

}
