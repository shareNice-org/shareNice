#!/usr/bin/perl -w
use LWP::UserAgent;

#Please enter in the domain, without the protocol [http(s)] and without the trailing slash below
my $hostedDomain = "sharenice.org";

if (scalar(@ARGV) == 1) {
    $hostedDomain = $ARGV[0];
}
print STDERR "Generating code.js to be hosted at $hostedDomain\n";

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
            $output .= "    \"url\" : \"".$details[2]."\"\n";
            $output .= "    ,\"icon\" : \"".$details[1]."\"\n" unless( "$details[1]" eq "$details[0].png" );
            $output .= "  },\n";
        }

    }
    $count++;
}

close(CONFIG);

$output =~ s/,\n$/\n/;

$output .= "};\n";

open (JSTEMPLATE, '<', 'code.js.template') || die "can't open the main .js file\n";

while (<JSTEMPLATE>) {
    s/%DOMAIN%/$hostedDomain/g;
    $output .= $_;
}

close(JSTEMPLATE);

#Get shortended output 
my $google = 'http://closure-compiler.appspot.com/compile';
my $ua = LWP::UserAgent->new;

my $googleres = $ua->post($google, 
            [ 'js_code' => $output,
            'compilation_level' => 'WHITESPACE_ONLY' ,
            'output_format' => 'text' ,
            'output_info' => 'compiled_code' ]
            ) ;
die "$google error: ", $googleres->status_line
    unless $googleres->is_success;

$output = $googleres->content;

open (OUTPUT, "> ../code.js") || die "sorry cant write to output file : ../code.js\n";
print OUTPUT $output;
close(OUTPUT);

print STDERR "Finished generating new code.js file\n";
# vi:set expandtab sts=4 sw=4:
