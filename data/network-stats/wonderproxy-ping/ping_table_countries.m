% name of input files
servers_file_csv = "servers-2020-07-19.csv";
pings_file_csv = "pings-2020-07-19-2020-07-20.csv";

countries_output_csv = "countries.csv";
latency_matrix_csv = "latency_matrix.csv";

%% load package for csv2cell() function
pkg load io

% load csv file for servers
servers = csv2cell(servers_file_csv);

% select ids and countries from input file
ids = cellfun(@(x) str2num(x)+1,{servers{2:end,1}}');
countries = {servers{2:end,6}};

clear servers

% make countries unique (each country can have multiple servers)
[countries_unique, ~, index2country] = unique(countries);

% save the unique country names
cell2csv(countries_output_csv, countries_unique)
clear countries

% make a vector that can map any id to a unique country names index 
id2index = ids(index2country);
id2cidx = zeros(1,max(ids));
id2cidx(ids) = index2country;

% calculate total number of unique country names
num_countries = length(countries_unique);

%% read ping round trip time values
ping = csv2cell(pings_file_csv);

% select important values of source server distenation server and ping rtt time
ping_len = size(ping,1);
ping_src_id = zeros(1,ping_len-1);
ping_dst_id = zeros(1,ping_len-1);
ping_rtt_avg = zeros(1,ping_len-1);
for i = 2:size(ping,1)
  ping_src_id(i-1) = str2double(ping(i,1))+1;
  ping_dst_id(i-1) = str2double(ping(i,2))+1;
  ping_rtt_avg(i-1) = str2double(ping(i,5));
endfor

% clear large cell of ping data to save memory
clear ping

% remove unknown server ids from ping data
unknown_servers = union(setdiff(ping_src_id,ids), setdiff(ping_dst_id,ids));
unknown_servers_lines = zeros(1,length(ping_src_id));
for i=1:length(unknown_servers)
  unknown_servers_lines = or(unknown_servers_lines, ping_src_id==unknown_servers(i));
  unknown_servers_lines = or(unknown_servers_lines, ping_dst_id==unknown_servers(i));
endfor
src_id = ping_src_id(~unknown_servers_lines);
dst_id = ping_dst_id(~unknown_servers_lines);
rtt_avg = ping_rtt_avg(~unknown_servers_lines);

% calculate average latency time by passing over the ping rtt times one time
avg_latency = zeros(num_countries);
num_already_seen = zeros(num_countries);
for i = 1:length(rtt_avg)
  if src_id(i) != dst_id(i)
    src = id2cidx(src_id(i));
    dst = id2cidx(dst_id(i));
    if num_already_seen(src, dst) == 0
      avg_latency(src, dst) = rtt_avg(i)/2;
      num_already_seen(src, dst) = 1;
    else
      num = num_already_seen(src, dst);
      old_avg = avg_latency(src, dst);
      new_avg = rtt_avg(i)/2;
      avg_latency(src, dst) = (num*old_avg + new_avg) / (num+1);
      num_already_seen(src, dst) += 1;
    endif
  endif
endfor

% change the latency inside each country with only 1 server to average of others
inside_country_latencies = logical(eye(num_countries));
one_server_countries = and(inside_country_latencies, avg_latency == 0);
many_server_countries = and(inside_country_latencies, avg_latency ~= 0);
inside_latency_avg = mean(avg_latency(many_server_countries));
avg_latency(one_server_countries) = inside_latency_avg;

% calculate average latency to be used for nodes from other locations not listed
avg_latency = [[avg_latency; mean(avg_latency)], [mean(avg_latency,2); mean(avg_latency(logical(eye(num_countries))))]];

csvwrite(latency_matrix_csv, avg_latency)
