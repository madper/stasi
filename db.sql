create table heartbeat(timestamp TIMESTAMPTZ not null,
deviceID TEXT not null,
containers text[] null,
city text null,
ip text null,
isp text null,
region text null)
