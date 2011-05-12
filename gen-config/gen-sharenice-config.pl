#!/usr/bin/perl -w

open(CONFIG, '<', 'sharenice-domains.txt') || die "cannot open test file: $!";

my $count = 0;

my $output = "{\n";

while(<CONFIG>) {
    if ($count != 0) {
        chomp;
        my @details = split(',', $_);

        if (defined($details[1])) {
            $output .= " \"".$details[0]."\" :\n";
            $output .= "  {\n";
            $output .= "    \"url\" : \"".$details[2]."\",\n";
            $output .= "    \"icon\" : \"".$details[1]."\"\n";
            $output .= "  },\n";
        }

    }
    $count++;
}
$output =~ s/,\n$/\n/;

$output .= "}\n";

print $output;

close(CONFIG);

# vi:set expandtab sts=4 sw=4:
