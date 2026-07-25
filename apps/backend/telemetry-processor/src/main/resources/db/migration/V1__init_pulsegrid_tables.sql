create table if not exists vehicle_events (
    event_id varchar(100) primary key,
    vehicle_id varchar(100) not null,
    event_timestamp timestamptz not null,
    latitude double precision not null,
    longitude double precision not null,
    speed_kph double precision not null,
    fuel_or_battery_level_pct double precision not null,
    odometer_km double precision not null,
    engine_diagnostic_code varchar(100),
    created_at timestamptz not null default now()
);

create index if not exists idx_vehicle_events_vehicle_id_event_timestamp
    on vehicle_events (vehicle_id, event_timestamp desc);

create table if not exists vehicle_status (
    vehicle_id varchar(100) primary key,
    last_seen_at timestamptz not null,
    latitude double precision not null,
    longitude double precision not null,
    speed_kph double precision not null,
    fuel_or_battery_level_pct double precision not null,
    engine_diagnostic_code varchar(100)
);

create table if not exists vehicle_alerts (
    alert_id bigserial primary key,
    vehicle_id varchar(100) not null,
    alert_type varchar(100) not null,
    severity varchar(50) not null,
    message text not null,
    triggered_at timestamptz not null,
    resolved boolean not null default false,
    resolved_at timestamptz
);

create index if not exists idx_vehicle_alerts_vehicle_id_triggered_at
    on vehicle_alerts (vehicle_id, triggered_at desc);
