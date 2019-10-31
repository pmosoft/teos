-- Drop table

-- DROP TABLE public.los_eng_result;

CREATE TABLE public.los_eng_result (
	job_id text NULL,
	scenario_id int4 NULL,
	sector_id text NULL,
	bin_id text NULL,
	bin_x float8 NULL,
	bin_y float8 NULL,
	bin_z float8 NULL,
	bld_id int4 NULL,
	los bool NULL,
	in_bld bool NULL,
	theta_deg int4 NULL,
	phi_deg int4 NULL,
	sector_x float8 NULL,
	sector_y float8 NULL,
	sector_z float8 NULL
);

-- Permissions

ALTER TABLE public.los_eng_result OWNER TO postgres;
GRANT ALL ON TABLE public.los_eng_result TO postgres;




-- Drop table

-- DROP TABLE public.los_bld_result;

CREATE UNLOGGED TABLE public.los_bld_result (
	scenario_id int4 NULL,
	sector_id text NULL,
	tbd text NULL,
	floor_x float8 NULL,
	floor_y float8 NULL,
	floor_z int4 NULL,
	los bool NULL
);
CREATE INDEX los_bld_result_idx ON public.los_bld_result USING btree (scenario_id, sector_id, tbd, floor_x, floor_y, floor_z);

-- Permissions

ALTER TABLE public.los_bld_result OWNER TO postgres;
GRANT ALL ON TABLE public.los_bld_result TO postgres;



-- Drop table

-- DROP TABLE public.scenario_nr_ru_dem;

CREATE TABLE public.scenario_nr_ru_dem (
	scenario_id text NULL,
	enb_id text NULL,
	pci text NULL,
	pci_port text NULL,
	ru_id text NULL,
	dem_heght float8 NULL,
	bld_avg_height float8 NULL
);

-- Permissions

ALTER TABLE public.scenario_nr_ru_dem OWNER TO postgres;
GRANT ALL ON TABLE public.scenario_nr_ru_dem TO postgres;


-- Drop table

-- DROP TABLE public.shp;

CREATE TABLE public.shp (
	gid serial NOT NULL,
	tbd_key varchar(20) NULL,
	bd_key varchar(20) NULL,
	bd_name varchar(100) NULL,
	bd_group varchar(6) NULL,
	bd_type varchar(14) NULL,
	bd_floor5 int2 NULL,
	bd_floor6 float8 NULL,
	bd_height float8 NULL,
	bd_3d_he8 float8 NULL,
	elevation float8 NULL,
	file_path varchar(254) NULL,
	file_name varchar(254) NULL,
	src varchar(4) NULL,
	geom geometry(MULTIPOLYGON, 5181) NULL,
	c_pos geometry(POINT) NULL,
	rect geometry NULL,
	part_id text NULL
)
PARTITION BY LIST (part_id);
CREATE INDEX shp_idx ON ONLY public.shp USING gist (c_pos);
CREATE INDEX shp_idx1 ON ONLY public.shp USING gist (geom);
CREATE INDEX shp_idx2 ON ONLY public.shp USING gist (rect);
CREATE INDEX shp_idx4 ON ONLY public.shp USING btree (file_name);

-- Permissions

ALTER TABLE public.shp OWNER TO postgres;
GRANT ALL ON TABLE public.shp TO postgres;

-- Drop table

-- DROP TABLE public.job_dis;

CREATE TABLE public.job_dis (
	job_from text NULL,
	job_id text NULL,
	scenario_id text NULL,
	schedule_id text NULL,
	ru_id text NULL,
	pos geometry(POINT) NULL,
	bld_id int4 NULL,
	part_id text[] NULL,
	stat int4 NULL,
	cluster_name text NULL,
	update_time timestamptz NULL, 
	created_at timestamptz NULL DEFAULT CURRENT_TIMESTAMP
)
PARTITION BY LIST (cluster_name);

-- Permissions

ALTER TABLE public.job_dis OWNER TO postgres;
GRANT ALL ON TABLE public.job_dis TO postgres;


