# Examples

- [Basic Examples](#basic-examples)
- [Web Application](#web-application)
- [Email Templates](#email-templates)
- [Blog System](#blog-system)
- [E-commerce Product Page](#ecommerce-product-page)
- [Dashboard with Charts](#dashboard-with-charts)
- [Multi-language Support](#multi-language-support)

<a name="basic-examples"></a>
## Basic Examples

### Hello World

**Java:**
```java
import com.hindbiswas.jhp.*;
import com.hindbiswas.jhp.engine.*;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder().build();
        FunctionLibrary lib = new FunctionLibrary();
        JhpEngine engine = new JhpEngine(settings, lib);
        
        Context ctx = new Context();
        ctx.add("name", "World");
        
        Files.writeString(Path.of("hello.jhp"), "Hello, {{ name }}!");
        String output = engine.render("hello.jhp", ctx);
        System.out.println(output);
    }
}
```

**Output:**
```
Hello, World!
```

### Simple Page

**template.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>{{ title }}</title>
</head>
<body>
    <h1>{{ heading }}</h1>
    <p>{{ content }}</p>
</body>
</html>
```

**Java:**
```java
Context ctx = new Context();
ctx.add("title", "My Page");
ctx.add("heading", "Welcome");
ctx.add("content", "This is a simple page.");

String html = engine.render("template.jhp", ctx);
```

<a name="web-application"></a>
## Web Application

### User Profile Page

**profile.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>{{ user.name }}'s Profile</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    {% include 'partials/header.jhp' %}
    
    <main class="profile">
        <div class="profile-header">
            <img src="{{ user.avatar }}" alt="{{ user.name }}">
            <h1>{{ user.name }}</h1>
            {% if (user.verified) %}
                <span class="badge verified">✓ Verified</span>
            {% endif %}
        </div>
        
        <div class="profile-info">
            <p><strong>Email:</strong> {{ user.email }}</p>
            <p><strong>Member since:</strong> {{ user.joinDate }}</p>
            <p><strong>Posts:</strong> {{ len(user.posts) }}</p>
        </div>
        
        <section class="posts">
            <h2>Recent Posts</h2>
            {% if (len(user.posts) > 0) %}
                <ul>
                {% foreach (post in user.posts) %}
                    <li>
                        <h3>{{ post.title }}</h3>
                        <p>{{ post.excerpt }}</p>
                        <a href="/post/{{ post.id }}">Read more</a>
                    </li>
                {% endforeach %}
                </ul>
            {% else %}
                <p>No posts yet.</p>
            {% endif %}
        </section>
    </main>
    
    {% include 'partials/footer.jhp' %}
</body>
</html>
```

**Java Controller:**
```java
@GetMapping("/profile/{username}")
public ResponseEntity<String> profile(@PathVariable String username) {
    try {
        User user = userService.findByUsername(username);
        
        Context ctx = new Context();
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("avatar", user.getAvatarUrl());
        userData.put("verified", user.isVerified());
        userData.put("joinDate", user.getJoinDate());
        userData.put("posts", user.getRecentPosts());
        ctx.add("user", userData);
        
        String html = engine.render("profile.jhp", ctx);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error loading profile");
    }
}
```

<a name="email-templates"></a>
## Email Templates

### Welcome Email

**welcome-email.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; }
        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
        .header { background: #4CAF50; color: white; padding: 20px; text-align: center; }
        .content { padding: 20px; background: #f9f9f9; }
        .button { display: inline-block; padding: 10px 20px; background: #4CAF50; 
                  color: white; text-decoration: none; border-radius: 5px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Welcome to {{ siteName }}!</h1>
        </div>
        <div class="content">
            <p>Hi {{ userName }},</p>
            <p>Thank you for joining {{ siteName }}. We're excited to have you on board!</p>
            
            <p>To get started, please verify your email address by clicking the button below:</p>
            
            <p style="text-align: center;">
                <a href="{{ activationLink }}" class="button">Verify Email</a>
            </p>
            
            <p>If the button doesn't work, copy and paste this link into your browser:</p>
            <p style="word-break: break-all;">{{ activationLink }}</p>
            
            <p>Best regards,<br>The {{ siteName }} Team</p>
        </div>
    </div>
</body>
</html>
```

**Java:**
```java
public class EmailService {
    private final JhpEngine engine;
    
    public void sendWelcomeEmail(User user) throws Exception {
        Context ctx = new Context();
        ctx.add("siteName", "MyApp");
        ctx.add("userName", user.getName());
        ctx.add("activationLink", generateActivationLink(user));
        
        String html = engine.render("welcome-email.jhp", ctx);
        emailClient.send(user.getEmail(), "Welcome to MyApp!", html);
    }
}
```

<a name="blog-system"></a>
## Blog System

### Blog Post Page

**blog-post.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>{{ post.title }} - {{ siteName }}</title>
    <meta name="description" content="{{ post.excerpt }}">
</head>
<body>
    {% include 'partials/nav.jhp' %}
    
    <article class="blog-post">
        <header>
            <h1>{{ post.title }}</h1>
            <div class="meta">
                <span class="author">By {{ post.author.name }}</span>
                <span class="date">{{ post.publishDate }}</span>
                <span class="reading-time">{{ post.readingTime }} min read</span>
            </div>
            
            {% if (len(post.tags) > 0) %}
                <div class="tags">
                    {% foreach (tag in post.tags) %}
                        <a href="/tag/{{ tag }}" class="tag">{{ tag }}</a>
                    {% endforeach %}
                </div>
            {% endif %}
        </header>
        
        {% if (post.featuredImage) %}
            <img src="{{ post.featuredImage }}" alt="{{ post.title }}" class="featured-image">
        {% endif %}
        
        <div class="content">
            {{{ post.content }}}
        </div>
        
        <footer>
            <div class="share">
                <h3>Share this post</h3>
                <a href="https://twitter.com/share?url={{ post.url }}">Twitter</a>
                <a href="https://facebook.com/sharer?u={{ post.url }}">Facebook</a>
            </div>
        </footer>
    </article>
    
    <section class="comments">
        <h2>Comments ({{ len(comments) }})</h2>
        {% if (len(comments) > 0) %}
            {% foreach (comment in comments) %}
                <div class="comment">
                    <div class="comment-author">{{ comment.author }}</div>
                    <div class="comment-date">{{ comment.date }}</div>
                    <div class="comment-content">{{ comment.content }}</div>
                </div>
            {% endforeach %}
        {% else %}
            <p>No comments yet. Be the first to comment!</p>
        {% endif %}
    </section>
    
    {% include 'partials/footer.jhp' %}
</body>
</html>
```

**Java:**
```java
@GetMapping("/blog/{slug}")
public ResponseEntity<String> blogPost(@PathVariable String slug) {
    try {
        Post post = postService.findBySlug(slug);
        List<Comment> comments = commentService.findByPost(post.getId());
        
        Context ctx = new Context();
        ctx.add("siteName", "My Blog");
        ctx.add("post", post.toMap());
        ctx.add("comments", comments.stream()
            .map(Comment::toMap)
            .collect(Collectors.toList()));
        
        String html = engine.render("blog-post.jhp", ctx);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    } catch (Exception e) {
        return ResponseEntity.status(404).body("Post not found");
    }
}
```

<a name="ecommerce-product-page"></a>
## E-commerce Product Page

**product.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>{{ product.name }} - {{ storeName }}</title>
</head>
<body>
    {% include 'partials/header.jhp' %}
    
    <main class="product-page">
        <div class="product-gallery">
            <img src="{{ product.mainImage }}" alt="{{ product.name }}" class="main-image">
            {% if (len(product.images) > 1) %}
                <div class="thumbnails">
                    {% foreach (image in product.images) %}
                        <img src="{{ image }}" alt="{{ product.name }}">
                    {% endforeach %}
                </div>
            {% endif %}
        </div>
        
        <div class="product-info">
            <h1>{{ product.name }}</h1>
            
            <div class="rating">
                {% for (i = 0; i < product.rating; i++) %}
                    ★
                {% endfor %}
                <span>({{ product.reviewCount }} reviews)</span>
            </div>
            
            <div class="price">
                {% if (product.onSale) %}
                    <span class="original-price">${{ product.originalPrice }}</span>
                    <span class="sale-price">${{ product.price }}</span>
                    <span class="discount">{{ product.discountPercent }}% OFF</span>
                {% else %}
                    <span class="price">${{ product.price }}</span>
                {% endif %}
            </div>
            
            <div class="description">
                <p>{{ product.description }}</p>
            </div>
            
            <div class="stock">
                {% if (product.inStock) %}
                    <span class="in-stock">✓ In Stock</span>
                {% else %}
                    <span class="out-of-stock">Out of Stock</span>
                {% endif %}
            </div>
            
            {% if (product.inStock) %}
                <form action="/cart/add" method="POST">
                    <input type="hidden" name="productId" value="{{ product.id }}">
                    <label>Quantity:</label>
                    <input type="number" name="quantity" value="1" min="1" max="{{ product.stock }}">
                    <button type="submit" class="add-to-cart">Add to Cart</button>
                </form>
            {% endif %}
            
            <div class="features">
                <h3>Features</h3>
                <ul>
                {% foreach (feature in product.features) %}
                    <li>{{ feature }}</li>
                {% endforeach %}
                </ul>
            </div>
        </div>
    </main>
    
    <section class="related-products">
        <h2>You might also like</h2>
        <div class="product-grid">
            {% foreach (related in relatedProducts) %}
                <div class="product-card">
                    <img src="{{ related.image }}" alt="{{ related.name }}">
                    <h3>{{ related.name }}</h3>
                    <p class="price">${{ related.price }}</p>
                    <a href="/product/{{ related.slug }}">View Details</a>
                </div>
            {% endforeach %}
        </div>
    </section>
    
    {% include 'partials/footer.jhp' %}
</body>
</html>
```

<a name="dashboard-with-charts"></a>
## Dashboard with Charts

**dashboard.jhp:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - {{ appName }}</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    {% include 'partials/sidebar.jhp' %}
    
    <main class="dashboard">
        <h1>Welcome back, {{ user.name }}!</h1>
        
        <div class="stats-grid">
            {% foreach (stat in stats) %}
                <div class="stat-card">
                    <div class="stat-icon">{{ stat.icon }}</div>
                    <div class="stat-value">{{ stat.value }}</div>
                    <div class="stat-label">{{ stat.label }}</div>
                    {% if (stat.change) %}
                        <div class="stat-change {{ stat.change > 0 ? 'positive' : 'negative' }}">
                            {{ stat.change > 0 ? '↑' : '↓' }} {{ stat.change }}%
                        </div>
                    {% endif %}
                </div>
            {% endforeach %}
        </div>
        
        <div class="charts">
            <div class="chart-container">
                <h2>Sales Overview</h2>
                <canvas id="salesChart"></canvas>
            </div>
            
            <div class="chart-container">
                <h2>User Growth</h2>
                <canvas id="userChart"></canvas>
            </div>
        </div>
        
        <div class="recent-activity">
            <h2>Recent Activity</h2>
            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Action</th>
                        <th>User</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    {% foreach (activity in recentActivity) %}
                        <tr>
                            <td>{{ activity.date }}</td>
                            <td>{{ activity.action }}</td>
                            <td>{{ activity.user }}</td>
                            <td>
                                <span class="status {{ tolowercase(activity.status) }}">
                                    {{ activity.status }}
                                </span>
                            </td>
                        </tr>
                    {% endforeach %}
                </tbody>
            </table>
        </div>
    </main>
    
    <script>
        // Chart data from template
        const salesData = {{ salesChartData }};
        const userData = {{ userChartData }};
        
        // Initialize charts
        new Chart(document.getElementById('salesChart'), salesData);
        new Chart(document.getElementById('userChart'), userData);
    </script>
</body>
</html>
```

<a name="multi-language-support"></a>
## Multi-language Support

**Java:**
```java
public class I18nFunctionLibrary extends FunctionLibrary {
    private final Map<String, Map<String, String>> translations;
    
    public I18nFunctionLibrary() {
        super();
        this.translations = loadTranslations();
        
        register("t", (args, scopes) -> {
            if (args.isEmpty()) return "";
            
            String key = args.get(0).toString();
            String locale = "en"; // default
            
            // Get locale from scope
            for (Map<String, Object> scope : scopes) {
                if (scope.containsKey("locale")) {
                    locale = scope.get("locale").toString();
                    break;
                }
            }
            
            return translations
                .getOrDefault(locale, translations.get("en"))
                .getOrDefault(key, key);
        });
    }
}
```

**page.jhp:**
```html
<!DOCTYPE html>
<html lang="{{ locale }}">
<head>
    <title>{{ t("page.title") }}</title>
</head>
<body>
    <h1>{{ t("welcome.heading") }}</h1>
    <p>{{ t("welcome.message") }}</p>
    
    <nav>
        <a href="?lang=en">English</a>
        <a href="?lang=es">Español</a>
        <a href="?lang=fr">Français</a>
    </nav>
</body>
</html>
```

**Java:**
```java
Context ctx = new Context();
ctx.add("locale", request.getParameter("lang"));
String html = engine.render("page.jhp", ctx);
```

## More Examples

For more examples, check out the `examples/` directory in the repository.

## Next Steps

- Review [API Reference](api-reference.md)
- Explore [Advanced Usage](advanced-usage.md)
- Check the GitHub repository for more examples
