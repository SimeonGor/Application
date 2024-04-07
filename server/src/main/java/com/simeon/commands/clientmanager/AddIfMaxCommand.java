package com.simeon.commands.clientmanager;

import com.simeon.Response;
import com.simeon.collection.ICollectionManager;
import com.simeon.element.Organization;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Command to add new item to the collection if its value exceeds the value of the largest item in this collection
 */
@Log
public class AddIfMaxCommand extends Command<Organization> {
    public AddIfMaxCommand(ICollectionManager<Organization> collectionManager) {
        super(collectionManager, "add_if_max",
                "add a new item to the collection if its value exceeds the value of the largest item in this collection");
        addParameter("element", Organization.class);
    }

    @Override
    public Response execute(@NonNull HashMap<String, Object> parameters) {
        log.log(Level.FINE, "Add_if_max command started with ", parameters.toString());
        try {
            Organization element = (Organization) parameters.get("element");
            Comparator cmp = collectionManager.getComparator();

            if (collectionManager.isEmpty()
                    || cmp.compare(collectionManager.getStream().max(cmp).get(), element) < 0) {
                collectionManager.add(element);
                return new Response(true, "New item has been successful added to the collection");
            } else {
                return new Response(true, "New item is less than the maximum in the collection");
            }
        }
        catch (ClassCastException e) {
            return new Response(false, "Invalid type of parameters");
        }
    }
}
