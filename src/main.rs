// Adding modules
mod api;
mod models;
mod repository;

#[macro_use]
extern crate rocket;
use api::subject_api::{create_subject, get_all_subjects, get_subject_by_id, delete_subject};
use repository::mondodb_repo::MongoRepo;
use rocket::{get, http::Status, serde::json::Json};

#[get("/")]
fn hello() -> Result<Json<String>, Status> {
    Ok(Json(String::from("Hello from rust and mongodb")))
}

#[launch]
fn rocket() -> _ {
    let db = MongoRepo::init();
    rocket::build()
        .manage(db)
        .mount("/", routes![hello])
        .mount("/", routes![get_all_subjects])
        .mount("/", routes![create_subject])
        .mount("/", routes![get_subject_by_id])
        .mount("/", routes![delete_subject])
}
