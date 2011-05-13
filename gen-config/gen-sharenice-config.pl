#!/usr/bin/perl -w

open(CONFIG, '<', 'sharenice-domains.txt') || die "cannot open domain config file: $!";

my $count = 0;

my $output = "var shareNiceConfig = {\n";

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

close(CONFIG);

$output =~ s/,\n$/\n/;

$output .= "};\n";

open (JSTEMPLATE, '<', 'shareNice.js.template') || die "can't open the main .js file\n";

while (<JSTEMPLATE>) {
    $output .= $_;
}

close(JSTEMPLATE);

open (OUTPUT, "> ../code.js") || die "sorry cant write to output file : ../code.js\n";

print OUTPUT $output;

close(OUTPUT);

print STDERR "Finished generating new code.js file\n";
# vi:set expandtab sts=4 sw=4:
