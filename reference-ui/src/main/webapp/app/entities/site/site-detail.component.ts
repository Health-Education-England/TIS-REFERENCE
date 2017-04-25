import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {JhiLanguageService} from "ng-jhipster";
import {Site} from "./site.model";
import {SiteService} from "./site.service";

@Component({
	selector: 'jhi-site-detail',
	templateUrl: './site-detail.component.html'
})
export class SiteDetailComponent implements OnInit, OnDestroy {

	site: Site;
	private subscription: any;

	constructor(private jhiLanguageService: JhiLanguageService,
				private siteService: SiteService,
				private route: ActivatedRoute) {
		this.jhiLanguageService.setLocations(['site']);
	}

	ngOnInit() {
		this.subscription = this.route.params.subscribe(params => {
			this.load(params['id']);
		});
	}

	load(id) {
		this.siteService.find(id).subscribe(site => {
			this.site = site;
		});
	}

	previousState() {
		window.history.back();
	}

	ngOnDestroy() {
		this.subscription.unsubscribe();
	}

}
