import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiService {
  static const String baseUrl = "http://localhost:8081/auth";

  static Future<http.Response> register({
    required String email,
    required String password,
    required String firstName,
    required String lastName,
    required String birthDate,
  }) async {
    final url = Uri.parse("$baseUrl/register");

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
}
