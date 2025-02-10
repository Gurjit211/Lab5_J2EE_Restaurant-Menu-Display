$(document).ready(function() {
    // Function to load all menu items or search by query
    function loadMenuItems(query = "") {
        $.ajax({
            url: "menu", // Calls the MenuServlet
            method: "GET",
            dataType: "json",
            data: { search: query },  // Pass search query to servlet
            success: function(data) {
                var menuHtml = '';
                if (data.length === 0) {
                    menuHtml = '<p>No items found.</p>';
                } else {
                    data.forEach(function(item) {
                        menuHtml += '<div class="menu-item" data-id="' + item.id + '">';
                        menuHtml += '<h2>' + item.name + '</h2>';
                        menuHtml += '<p>' + item.description + '</p>';
                        menuHtml += '<span>$' + item.price.toFixed(2) + '</span>';
                        menuHtml += '<button class="delete-btn" data-id="' + item.id + '">Delete</button>';
                        menuHtml += '<button class="edit-btn" data-id="' + item.id + '" data-name="' + item.name + '" data-description="' + item.description + '" data-price="' + item.price + '">Edit</button>';
                        menuHtml += '</div>';
                    });
                }
                $('#menu-container').html(menuHtml);
            },
            error: function() {
                $('#menu-container').html('<p>Error loading menu items.</p>');
            }
        });
    }

    // Initial load of menu items
    loadMenuItems();

    // Search functionality
    $('#search-bar').on('input', function() {
        const query = $(this).val();
        loadMenuItems(query);
    });

    // Delete functionality
    $(document).on('click', '.delete-btn', function() {
        const itemId = $(this).data('id');
        if (confirm("Are you sure you want to delete this item?")) {
            $.ajax({
                url: "deleteMenu",
                method: "POST",
                data: { id: itemId },
                success: function() {
                    loadMenuItems();  // Reload the menu after deletion
                },
                error: function() {
                    alert("Failed to delete the menu item.");
                }
            });
        }
    });

    // Edit functionality
    $(document).on('click', '.edit-btn', function() {
        const itemId = $(this).data('id');
        const currentName = $(this).data('name');
        const currentDescription = $(this).data('description');
        const currentPrice = $(this).data('price');

        const newName = prompt("Enter new name:", currentName);
        const newDescription = prompt("Enter new description:", currentDescription);
        const newPrice = prompt("Enter new price:", currentPrice);

        if (newName && newDescription && newPrice) {
            $.ajax({
                url: "editMenu",
                method: "POST",
                data: {
                    id: itemId,
                    name: newName,
                    description: newDescription,
                    price: newPrice
                },
                success: function() {
                    loadMenuItems();  // Reload the menu after editing
                },
                error: function() {
                    alert("Failed to edit the menu item.");
                }
            });
        }
    });
});
