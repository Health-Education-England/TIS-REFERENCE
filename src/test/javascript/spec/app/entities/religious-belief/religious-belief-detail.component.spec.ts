import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {ReligiousBeliefDetailComponent} from "../../../../../../main/webapp/app/entities/religious-belief/religious-belief-detail.component";
import {ReligiousBeliefService} from "../../../../../../main/webapp/app/entities/religious-belief/religious-belief.service";
import {ReligiousBelief} from "../../../../../../main/webapp/app/entities/religious-belief/religious-belief.model";

describe('Component Tests', () => {

	describe('ReligiousBelief Management Detail Component', () => {
		let comp: ReligiousBeliefDetailComponent;
		let fixture: ComponentFixture<ReligiousBeliefDetailComponent>;
		let service: ReligiousBeliefService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [ReligiousBeliefDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					ReligiousBeliefService
				]
			}).overrideComponent(ReligiousBeliefDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(ReligiousBeliefDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(ReligiousBeliefService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new ReligiousBelief(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.religiousBelief).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
