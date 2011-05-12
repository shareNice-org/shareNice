#!/usr/bin/perl -w

my $pattern = shift;

open(DOMAINS, '<', 'domains.txt') || die "cannot open domains file: $!";

while(<DOMAINS>) {
	chomp;
	if ($pattern) {
		next unless /$pattern/;
	}
	if (/(.*) \[(.*)\]/) {
		my $domain = $1;
		my $url = $2;
		my $fn = $domain.'.png';
		system('curl', $url, '-o', '../images/'.$fn);
	} else {
		my $fn = $_.'.png';
		$fn =~ s/\//\./g;
		$fn =~ s/www\d?\.//g;
		system('curl', 'http://www.google.com/s2/favicons?domain='.$_, '-o', 'images/'.$fn);
	}
}
