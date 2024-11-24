{ pkgs, lib, config, inputs, ... }:

{
  languages.java.enable = true;
  languages.java.jdk.package = pkgs.zulu11;

    # https://devenv.sh/scripts/
  scripts.hello.exec = ''
    echo hello from nix
  '';
}
