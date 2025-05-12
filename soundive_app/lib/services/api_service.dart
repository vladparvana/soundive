import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class ApiService {
  static const String baseUrl = "http://localhost:8080";

  static Future<http.Response> register({
    required String email,
    required String password,
    required String firstName,
    required String lastName,
    required String birthDate,
  }) async {
    final url = Uri.parse("$baseUrl/auth/register");

    final body = jsonEncode({
      "email": email,
      "password": password,
      "firstName": firstName,
      "lastName": lastName,
      "birthDate": birthDate,
    });

    final headers = {"Content-Type": "application/json"};

    return await http.post(url, headers: headers, body: body);
  }

  static Future<http.Response> login({
    required String email,
    required String password,
  }) async {
    final url = Uri.parse("$baseUrl/auth/login");

    final body = jsonEncode({"email": email, "password": password});

    final headers = {"Content-Type": "application/json"};

    final response = await http.post(url, headers: headers, body: body);

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final token = data["token"];

      final prefs = await SharedPreferences.getInstance();
      await prefs.setString("auth_token", token);
    }

    return response;
  }

  static Future<String?> getAuthToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString("auth_token");
  }

  static Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove("auth_token");
  }
}
